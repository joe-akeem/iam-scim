package com.airlock.iam.scim.handler;

import com.airlock.iam.common.api.domain.model.user.PersistentUser;
import com.airlock.iam.core.misc.impl.persistency.SimplePersistentUserWithPassword;
import de.captaingoldfish.scim.sdk.common.constants.HttpStatus;
import de.captaingoldfish.scim.sdk.common.exceptions.DocumentValidationException;
import de.captaingoldfish.scim.sdk.common.resources.User;
import de.captaingoldfish.scim.sdk.common.resources.complex.Name;
import de.captaingoldfish.scim.sdk.common.resources.multicomplex.Address;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// FIXME: the PersistentUser.contextData to User mapping is what we should take from the medusa configuration
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PersistentUserMapper {

    @Mapping(target = "id", source = "name")
    @Mapping(target = "meta.created", expression = "java(java.time.Instant.now())"/* FIXME: , qualifiedByName = "dateConverter", source = "createdAt"*/)
    @Mapping(target = "meta.lastModified", expression = "java(java.time.Instant.now())"/* FIXME: , qualifiedByName = "dateConverter", source = "updatedAt"*/)
    @Mapping(target = "externalId", ignore = true) // FIXME: we need to return this
    @Mapping(target = "userName", source = "name")
    @Mapping(target = "name", source = "contextData", qualifiedByName = "contextDataToNameConverter")
    @Mapping(target = "active" , expression = "java(!user.isLocked())")
    User persistentUserToUser(PersistentUser user);


    @Mapping(target = "name", source = "user", qualifiedByName = "usernameConverter")
    @Mapping(target = "locked" , expression = "java(!user.isActive().orElse(true))")
    @Mapping(target = "roles", ignore = true)
    // FIXME: we need to save the externalid
    SimplePersistentUserWithPassword userToPersistentUser(User user);

    List<User> persistentUsersToUsers(List<PersistentUser> users);

    List<PersistentUser> usersToPersistentUsers(List<User> dtos);

    @Named("dateConverter")
    default OffsetDateTime dateConverter(Instant instant) {
        return instant.atOffset(ZoneOffset.systemDefault().getRules().getOffset(instant));
    }

    @AfterMapping
    default void mapContextDataToPersistentUser(@MappingTarget SimplePersistentUserWithPassword persistentUser, User user) {
        user.getName().ifPresent(name -> mapNameToContextData(name, persistentUser));
        user.getPreferredLanguage().ifPresent(preferredLanguage -> persistentUser.putContextDataValue("language", preferredLanguage));
    }

    default void mapNameToContextData(Name name, SimplePersistentUserWithPassword persistentUser) {
        name.getGivenName().ifPresent(givenName -> persistentUser.putContextDataValue("givenname", givenName));
        name.getFamilyName().ifPresent(familyName -> persistentUser.putContextDataValue("surname", familyName));
    }

    @Named("contextDataToNameConverter")
    default Name contextDataToNameConverter(Map<String, Object> contextData) {
        String givenname = contextData.get("givenname").toString();
        String surname = contextData.get("surname").toString();

        if (givenname != null || surname != null) {
            return Name.builder()
                    .givenName(givenname)
                    .familyName(surname)
                    .build();
        }
        return null;
    }

    @Named("usernameConverter")
    default String usernameConverter(User user) {
        Optional<String> userId = user.getId();
        Optional<String> userName = user.getUserName();

        if (userId.isPresent() && userName.isPresent() && !userId.get().equals(userName.get()) ) {
            // there's our problem with the missing ID in the MEDUSA_USER table again...
            throw new DocumentValidationException("In IAM the username must be the same as the ID", HttpStatus.BAD_REQUEST, null);
        }

        return userId.orElse(userName.orElseThrow(() -> new DocumentValidationException("username is required", HttpStatus.BAD_REQUEST, null)));
    }
}
