package com.airlock.iam.scim.handler;

import com.airlock.iam.common.api.domain.model.store.user.DeletingUserStore;
import com.airlock.iam.common.api.domain.model.store.user.InsertingUserStore;
import com.airlock.iam.common.api.domain.model.user.PersistentUser;
import de.captaingoldfish.scim.sdk.common.constants.HttpStatus;
import de.captaingoldfish.scim.sdk.common.constants.enums.SortOrder;
import de.captaingoldfish.scim.sdk.common.exceptions.ConflictException;
import de.captaingoldfish.scim.sdk.common.exceptions.DocumentValidationException;
import de.captaingoldfish.scim.sdk.common.exceptions.InternalServerException;
import de.captaingoldfish.scim.sdk.common.exceptions.ResourceNotFoundException;
import de.captaingoldfish.scim.sdk.common.resources.User;
import de.captaingoldfish.scim.sdk.common.schemas.SchemaAttribute;
import de.captaingoldfish.scim.sdk.server.endpoints.Context;
import de.captaingoldfish.scim.sdk.server.endpoints.ResourceHandler;
import de.captaingoldfish.scim.sdk.server.filter.FilterNode;
import de.captaingoldfish.scim.sdk.server.response.PartialListResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airlock.iam.base.api.domain.model.user.UserId.userId;

@Service
public class UserHandler extends ResourceHandler<User> {

    private final PersistentUserMapper persistentUserMapper;
    public final InsertingUserStore insertingUserStore;
    public final DeletingUserStore deletingUserStore;

    public UserHandler(
            PersistentUserMapper persistentUserMapper,
            InsertingUserStore insertingUserStore,
            DeletingUserStore deletingUserStore
    ) {
        this.persistentUserMapper = persistentUserMapper;
        this.insertingUserStore = insertingUserStore;
        this.deletingUserStore = deletingUserStore;
    }

    @Override
    @Transactional
    public User createResource(User user, Context context) {
        insertingUserStore.findByIdentity(userId(user.getUserName().orElseThrow(() -> new DocumentValidationException("username is required", HttpStatus.BAD_REQUEST, null))))
                .ifPresent(persistedUser -> {
                    throw new ConflictException("resource with id '" + user.getUserName() + "' does already exist");
                });

        insertingUserStore.insert(persistentUserMapper.userToPersistentUser(user));
        PersistentUser persistentUser = insertingUserStore.findByIdentity(userId(user.getUserName().get())).orElseThrow(() -> new InternalServerException("Resource wasn't inserted as expected"));
        return persistentUserMapper.persistentUserToUser(persistentUser);
    }

    @Override
    public User getResource(String id, List<SchemaAttribute> attributes, List<SchemaAttribute> excludedAttributes, Context context) {
        return insertingUserStore.findByIdentity(userId(id)).map(persistentUserMapper::persistentUserToUser).orElse(null);
    }

    @Override
    public PartialListResponse<User> listResources(long startIndex, int count, FilterNode filter, SchemaAttribute sortBy, SortOrder sortOrder, List<SchemaAttribute> attributes, List<SchemaAttribute> excludedAttributes, Context context) {
        List<PersistentUser> persistentUsers = insertingUserStore.find().list();
        return PartialListResponse.<User>builder().resources(persistentUserMapper.persistentUsersToUsers(persistentUsers)).totalResults(persistentUsers.size()).build();
    }

    @Override
    @Transactional
    public User updateResource(User resourceToUpdate, Context context) {
        insertingUserStore.update(persistentUserMapper.userToPersistentUser(resourceToUpdate));
        PersistentUser persistentUser = insertingUserStore.findByIdentity(userId(resourceToUpdate.getId().orElseThrow())).orElseThrow(() -> new InternalServerException("Resource wasn't inserted as expected"));
        return persistentUserMapper.persistentUserToUser(persistentUser);

    }

    @Override
    @Transactional
    public void deleteResource(String id, Context context) {
        insertingUserStore.findByIdentity(userId(id)).orElseThrow(() -> new ResourceNotFoundException("resource with id '" + id + "' does not exist"));
        deletingUserStore.delete(id);
    }
}
