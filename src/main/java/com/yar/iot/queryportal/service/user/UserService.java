package com.yar.iot.queryportal.service.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.yar.iot.queryportal.common.PaginationResponse;
import com.yar.iot.queryportal.common.request.user.UserRequest;
import com.yar.iot.queryportal.common.response.user.UserResponse;
import com.yar.iot.queryportal.domain.document.QUserDocument;
import com.yar.iot.queryportal.domain.document.UserDocument;
import com.yar.iot.queryportal.exception.BusinessException;
import com.yar.iot.queryportal.repository.user.UserRepository;
import com.yar.iot.queryportal.service.BaseService;
import com.yar.iot.queryportal.service.CrudService;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.yar.iot.queryportal.exception.UserErrorMessage.NO_AUTHORITY_TO_EDIT_USER;
import static com.yar.iot.queryportal.exception.UserErrorMessage.OLD_PASSWORD_IS_INCORRECT;
import static com.yar.iot.queryportal.exception.UserErrorMessage.THE_USER_ID_MUST_BE_DETERMINED;
import static com.yar.iot.queryportal.exception.UserErrorMessage.USERNAME_IS_DUPLICATED;
import static com.yar.iot.queryportal.exception.UserErrorMessage.USER_NOT_FOUND;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * User service
 * */
@Service
@AllArgsConstructor
public class UserService
        extends BaseService
        implements UserDetailsService,
        CrudService<UserRequest, UserResponse, UserDocument> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Change password
     *
     * @param id The user ID
     * @param oldPassword The old password
     * @param newPassword The new password
     * @return {@link UserResponse} response
     * */
    public UserResponse changePassword(String id, String oldPassword, String newPassword) {
        UserDocument user = getUserById(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(OLD_PASSWORD_IS_INCORRECT);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return makeResponse(userRepository.save(user));
    }

    /**
     * Get user by given username
     *
     * @param username The username
     * @return {@link UserDocument} response
     * */
    public UserDocument getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    /**
     * Get user by given ID
     *
     * @param id The user ID
     * @return {@link UserDocument} response
     * */
    public UserDocument getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    /**
     * Validate user existence
     *
     * @param username The username
     * @return The user existence validation
     * */
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Load user by given username
     *
     * @param username The username
     * @return {@link UserDocument} response
     * */
    @Override
    public UserDocument loadUserByUsername(String username) {
        return getUserByUsername(username);
    }

    /**
     * Add new user
     *
     * @param request The user request
     * @return {@link UserResponse} response
     * */
    @Override
    public UserResponse add(UserRequest request) throws BusinessException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(USERNAME_IS_DUPLICATED);
        }
        return makeResponse(userRepository.save(makeEntity(request)));
    }

    /**
     * Edit a user
     *
     * @param id The user ID
     * @param request The user request
     * @return {@link UserResponse} response
     * */
    @Override
    public UserResponse edit(String id, UserRequest request) throws BusinessException {
        if (isBlank(id)) {
            throw new BusinessException(THE_USER_ID_MUST_BE_DETERMINED);
        }
        UserDocument user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        if (!(isCurrentUserAdmin() || getUserByUsername(getCurrentUser().getName()).getId().equals(id)) ||
                (!user.isActive() && !isCurrentUserAdmin())){
            throw new BusinessException(NO_AUTHORITY_TO_EDIT_USER);
        }
        userRepository.findByUsername(request.getUsername()).ifPresent(byUsername -> {
            if (!byUsername.getId().equals(id)) {
                throw new BusinessException(USERNAME_IS_DUPLICATED);
            }
        });
        UserDocument userDocument = makeEntity(request);
        userDocument.setId(id);
        return makeResponse(userDocument);
    }
    /**
     * Delete a user
     *
     * @param id The user ID
     * @return {@link UserResponse} response
     * */
    @Override
    public void delete(String id) throws BusinessException {
        activation(id,Boolean.FALSE);
    }

    /**
     * Activate/Deactivate a user
     *
     * @param id The user ID
     * @return {@link UserResponse} response
     * */
    @Override
    public void activation(String id, boolean isActive) throws BusinessException {
        UserDocument user = getUserById(id);
        user.setActive(Boolean.FALSE);
        userRepository.save(user);
    }

    /**
     * Get a user by given ID
     *
     * @param id The user ID
     * @return {@link UserResponse} response
     * */
    @Override
    public UserResponse get(String id) throws BusinessException {
        UserDocument user = getUserById(id);
        if (!user.isActive() && !isCurrentUserAdmin()) {
            throw new BusinessException(USER_NOT_FOUND);
        }
        return makeResponse(user);
    }


    /**
     * Add new user
     *
     * @param search The search filter
     * @param pageable The Pageable
     * @return {@link UserResponse} response
     * */
    @Override
    public PaginationResponse<UserResponse> list(String search, Pageable pageable) throws BusinessException {
        QUserDocument qUser = new QUserDocument("user");

        BooleanExpression criteriaExpression =
                qUser.username.contains(search)
                        .or(qUser.email.contains(search))
                        .or(qUser.name.contains(search))
                        .or(qUser.lastname.contains(search));
        if (!isCurrentUserAdmin()) {
            criteriaExpression.and(qUser.active.isTrue());
        }
        Page<UserDocument> page = userRepository.findAll(criteriaExpression, pageable);

        return PaginationResponse.<UserResponse>builder()
                .list(page.get().map(this::makeResponse).collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalPage(page.getTotalPages())
                .totalSize(page.getTotalElements())
                .build();
    }

    /**
     * Map the user document to the user response
     *
     * @param userDocument The user document
     * @return {@link UserResponse} response
     * */
    @Override
    public UserResponse makeResponse(UserDocument userDocument) throws BusinessException {
        return UserResponse.builder()
                .id(userDocument.getId())
                .active(userDocument.isActive())
                .authority(userDocument.getAuthority())
                .email(userDocument.getEmail())
                .createdBy(userDocument.getCreatedBy())
                .modifiedBy(userDocument.getModifiedBy())
                .lastname(userDocument.getLastname())
                .modifiedTime(userDocument.getModifiedTime())
                .name(userDocument.getName())
                .username(userDocument.getUsername())
                .createdTime(userDocument.getCreatedTime())
                .build();
    }

    /**
     * Map the user request to the user document
     *
     * @param request The user document
     * @return {@link UserDocument} response
     * */
    @Override
    public UserDocument makeEntity(UserRequest request) throws BusinessException {
        UserDocument entity = new UserDocument();
        entity.setUsername(request.getUsername());
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setEmail(request.getEmail());
        entity.setAuthority(request.getAuthority());
        entity.setEmail(request.getEmail());
        entity.setName(request.getName());
        entity.setLastname(request.getLastname());
        return entity;
    }

}
