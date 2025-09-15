package com.airlock.iam.scim.handler;

import com.airlock.iam.scim.jpa.entity.MedusaUser;
import de.captaingoldfish.scim.sdk.common.resources.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "id", source = "userName")
    @Mapping(target = "preferredLanguage", source = "language")
    @Mapping(target = "meta.created", source = "createdAt", qualifiedByName = "dateConverter")
    @Mapping(target = "name.givenName", source = "givenName")
    @Mapping(target = "name.familyName", source = "surname")
    @Mapping(target = "meta.lastModified", source = "updatedAt", qualifiedByName = "dateConverter")

    @Mapping(target = "externalId", ignore = true) // FIXME: we need to return this

    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "nickName", ignore = true)
    @Mapping(target = "profileUrl", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "locale", ignore = true)
    @Mapping(target = "timeZone", ignore = true)
    @Mapping(target = "phoneNumbers", ignore = true)
    @Mapping(target = "ims", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "entitlements", ignore = true)
    @Mapping(target = "enterpriseUser", ignore = true)

    // FIXME: fix these:
    @Mapping(target = "emails", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "x509Certificates", ignore = true)
    User medusaUserToUser(MedusaUser user);


    @Mapping(target = "userName", expression = "java(user.getUserName().orElse(null))")
    @Mapping(target = "surname", expression = "java(user.getName().flatMap(name -> name.getFamilyName()).orElse(null))")
    @Mapping(target = "givenName", expression = "java(user.getName().flatMap(name -> name.getGivenName()).orElse(null))")
    @Mapping(target = "language", expression = "java(user.getPreferredLanguage().orElse(null))")
    @Mapping(target = "password", expression = "java(user.getPassword().orElse(null))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active" , expression = "java(user.isActive())")

    // FIXME: fix these:
    @Mapping(target = "street", ignore = true)
    @Mapping(target = "streetnumber", ignore = true)
    @Mapping(target = "zipcode", ignore = true)
    @Mapping(target = "town", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "certX509Data", ignore = true)
    // FIXME: we need to save the externalid
    MedusaUser userToMedusaUser(User user);

    List<User> medusaUsersToUsers(List<MedusaUser> users);

    List<MedusaUser> usersToMedusaUsers(List<User> dtos);

    @Named("dateConverter")
    default OffsetDateTime dateConverter(Instant instant) {
        return instant.atOffset(ZoneOffset.systemDefault().getRules().getOffset(instant));
    }
}
