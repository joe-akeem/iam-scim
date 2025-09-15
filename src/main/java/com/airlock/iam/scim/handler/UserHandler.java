package com.airlock.iam.scim.handler;

import com.airlock.iam.scim.jpa.entity.MedusaUser;
import com.airlock.iam.scim.jpa.repository.MedusaUserRepository;
import de.captaingoldfish.scim.sdk.common.constants.HttpStatus;
import de.captaingoldfish.scim.sdk.common.constants.enums.SortOrder;
import de.captaingoldfish.scim.sdk.common.exceptions.ConflictException;
import de.captaingoldfish.scim.sdk.common.exceptions.DocumentValidationException;
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

@Service
public class UserHandler extends ResourceHandler<User> {

    private final MedusaUserRepository medusaUserRepository;
    private final UserMapper userMapper;

    public UserHandler(MedusaUserRepository medusaUserRepository, UserMapper userMapper) {
        this.medusaUserRepository = medusaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User createResource(User user, Context context) {
        if (medusaUserRepository.existsMedusaUserByUserName(user.getUserName().orElseThrow(() -> new DocumentValidationException("username is required", HttpStatus.BAD_REQUEST, null)))) {
            throw new ConflictException("resource with id '" + user.getUserName() + "' does already exist");
        }
        MedusaUser medusaUser = medusaUserRepository.save(userMapper.userToMedusaUser(user));
        return userMapper.medusaUserToUser(medusaUser);
    }

    @Override
    public User getResource(String id, List<SchemaAttribute> attributes, List<SchemaAttribute> excludedAttributes, Context context) {
        return userMapper.medusaUserToUser(medusaUserRepository.getMedusaUserByUserName(id));
    }

    @Override
    public PartialListResponse<User> listResources(long startIndex, int count, FilterNode filter, SchemaAttribute sortBy, SortOrder sortOrder, List<SchemaAttribute> attributes, List<SchemaAttribute> excludedAttributes, Context context) {
        List<MedusaUser> medusaUsers = medusaUserRepository.findAll();
        return PartialListResponse.<User>builder().resources(userMapper.medusaUsersToUsers(medusaUsers)).totalResults(medusaUsers.size()).build();
    }

    @Override
    @Transactional
    public User updateResource(User resourceToUpdate, Context context) {
        String id = resourceToUpdate.getId().orElseThrow(() -> new DocumentValidationException("id is required", HttpStatus.BAD_REQUEST, null));
        String userName = resourceToUpdate.getId().orElseThrow(() -> new DocumentValidationException("userName is required", HttpStatus.BAD_REQUEST, null));
        if (!id.equals(userName)) {
            // there's our problem with the missing ID in the MEDUSA_USER table again...
            throw new DocumentValidationException("In Airlock IAM the username cannot be changed. It is the unique ID", HttpStatus.BAD_REQUEST, null);
        }
        MedusaUser medusaUser = medusaUserRepository.getMedusaUserByUserName(resourceToUpdate.getUserName().orElseThrow(() -> new DocumentValidationException("username is required", HttpStatus.BAD_REQUEST, null)));
        if (medusaUser == null) {
            throw new ResourceNotFoundException("resource with id '" + resourceToUpdate.getUserName() + "' does not exist");
        }
        return userMapper.medusaUserToUser(medusaUserRepository.save(medusaUser));
    }

    @Override
    @Transactional
    public void deleteResource(String id, Context context) {
        if (!medusaUserRepository.existsMedusaUserByUserName(id)) {
            throw new ResourceNotFoundException("resource with id '" + id + "' does not exist");
        }
        medusaUserRepository.deleteMedusaUserByUserName(id);
    }
}
