package com.yar.iot.queryportal.service.user;


import com.yar.iot.queryportal.common.request.user.UserRequest;
import com.yar.iot.queryportal.common.response.user.UserResponse;
import com.yar.iot.queryportal.config.TestConfig;
import com.yar.iot.queryportal.domain.document.UserDocument;
import com.yar.iot.queryportal.domain.enums.Authority;
import com.yar.iot.queryportal.exception.BusinessException;
import com.yar.iot.queryportal.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.yar.iot.queryportal.exception.UserErrorMessage.THE_USER_ID_MUST_BE_DETERMINED;
import static com.yar.iot.queryportal.exception.UserErrorMessage.USERNAME_IS_DUPLICATED;
import static com.yar.iot.queryportal.exception.UserErrorMessage.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserService}
 * */
@DataMongoTest
@ExtendWith(value = {SpringExtension.class})
@Import(TestConfig.class)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp (){
        this.passwordEncoder = mock(PasswordEncoder.class);
        this.userService = new UserService(userRepository, passwordEncoder);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "test"));
    }

    @Test
    void shouldAddNewUser() {
        //given
        UserRequest request = new UserRequest("TestUserNameForAddNewUser" ,
                "TestPasswordForAddNewUser",
                Authority.USER,
                "TestForAddNewUser@company.com",
                "TestNameForAddNewUser",
                "TestLastNameForAddNewUser");

        // when
        UserResponse response = userService.add(request);

        //then
        then(response.getId())
                .as("Validate the id has generated.")
                .isNotBlank();
        then(response.getUsername())
                .as("Validate the username as saved same find.")
                .isEqualTo(request.getUsername());
        then(response.getEmail())
                .as("Validate the email as saved same find.")
                .isEqualTo(request.getEmail());
        then(response.getName())
                .as("Validate the name as saved same find.")
                .isNotNull();
        then(response.getLastname())
                .as("Validate the lastname as saved same find.")
                .isNotNull();
        then(response.getActive())
                .as("Validate user is active.")
                .isTrue();
        then(response.getCreatedTime())
                .as("Validate the created time has generated.")
                .isNotNull();
        then(response.getModifiedTime())
                .as("Validate the modified time has generated.")
                .isNotNull();
        then(response.getCreatedBy())
                .as("Validate the created by has generated.")
                .isNotNull();
        then(response.getModifiedBy())
                .as("Validate the modified by has generated.")
                .isNotNull();
    }

    @Test
    void shouldNotAddNewUserWithDuplicateUsername() {
        //given
        UserRequest request = new UserRequest("AddNewUserWithDuplicateTitle" ,
                "AddNewUserWithDuplicateTitle",
                Authority.USER,
                "AddNewUserWithDuplicateTitle@company.com",
                "AddNewUserWithDuplicateTitle",
                "AddNewUserWithDuplicateTitle");
        userService.add(request);

        // when
        Throwable throwable = catchThrowable(() -> userService.add(request));

        //then
        then(throwable)
                .as("Validate the instance of exception.")
                .isInstanceOf(BusinessException.class);
        then(((BusinessException) throwable).getErrorMessage())
                .as("Validate the error message.")
                .isEqualTo(USERNAME_IS_DUPLICATED);
    }

    @Test
    void shouldEditAUser() {
        //given
        UserRequest request = new UserRequest("AddNewUserForEdit" ,
                "AddNewUserForEdit",
                Authority.USER,
                "AddNewUserForEdit@company.com",
                "AddNewUserForEdit",
                "AddNewUserForEdit");
        UserResponse addedUser = userService.add(request);
        request.setEmail("EditedEmail@company.com");
        request.setName("EditedName");
        request.setLastname("EditedLastname");
        request.setAuthority(Authority.ADMIN);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("AddNewUserForEdit", "AddNewUserForEdit"));

        // when
        UserResponse response = userService.edit(addedUser.getId(), request);

        //then
        then(response.getId())
                .as("Validate the id has generated.")
                .isNotBlank();
        then(response.getEmail())
                .as("Validate the email as saved same find.")
                .isEqualTo(request.getEmail());
        then(response.getName())
                .as("Validate the name as saved same find.")
                .isEqualTo(request.getName());
        then(response.getLastname())
                .as("Validate the last name as saved same find.")
                .isEqualTo(request.getLastname());
        then(response.getAuthority())
                .as("Validate the last authority as saved same find.")
                .isEqualTo(request.getAuthority());
    }

    @Test
    void shouldNotEditUserWithoutID() {
        //given
        UserRequest request = new UserRequest("TestUser" ,
                "TestUser",
                Authority.USER,
                "TestUser@company.com",
                "TestUser",
                "TestUser");

        // when
        Throwable throwable = catchThrowable(() -> userService.edit(null, request));

        //then
        then(throwable)
                .as("Validate the instance of exception.")
                .isInstanceOf(BusinessException.class);
        then(((BusinessException) throwable).getErrorMessage())
                .as("Validate the error message.")
                .isEqualTo(THE_USER_ID_MUST_BE_DETERMINED);
    }

    @Test
    void shouldDelete() {
        //given
        UserRequest request = new UserRequest("TestUserForDeletion" ,
                "TestUserForDeletion",
                Authority.USER,
                "TestUserForDeletion@company.com",
                "TestUserForDeletion",
                "TestUserForDeletion");
        UserResponse added = userService.add(request);

        // when
        userService.delete(added.getId());

        //then
        Optional<UserDocument> userDocumentOptional = userRepository.findById(added.getId());
        then(userDocumentOptional.isPresent() && !userDocumentOptional.get().isActive())
                .as("Validate the user has been deleted.")
                .isTrue();
    }

    @Test
    void shouldNotDeleteWithWrongID() {
        // when
        Throwable throwable = catchThrowable(() -> userService.delete("WrongID"));

        // then
        then(throwable)
                .as("Validate the instance of exception.")
                .isInstanceOf(BusinessException.class);
        then(((BusinessException) throwable).getErrorMessage())
                .as("Validate the error message.")
                .isEqualTo(USER_NOT_FOUND);
    }

    @Test
    void shouldGet() {
        //given
        UserRequest request = new UserRequest("TestUserForGet" ,
                "TestUserForGet",
                Authority.USER,
                "TestUserForGet@company.com",
                "TestUserForGet",
                "TestUserForGet");
        UserResponse expected = userService.add(request);

        // when
        UserResponse response = userService.get(expected.getId());

        //then
        then(response.getId())
                .as("Validate the id as saved same find.")
                .isEqualTo(expected.getId());
        then(response.getUsername())
                .as("Validate the username as saved same find.")
                .isEqualTo(expected.getUsername());
    }

    @Test
    void shouldNotGetWithWrongID() {
        // when
        Throwable throwable = catchThrowable(() -> userService.get("WrongID"));

        // then
        then(throwable)
                .as("Validate the instance of exception.")
                .isInstanceOf(BusinessException.class);
        then(((BusinessException) throwable).getErrorMessage())
                .as("Validate the error message.")
                .isEqualTo(USER_NOT_FOUND);
    }

    @Test
    void shouldChangePassword() {
        //given
        final String oldPassword = "oldPassword";
        final String newPassword = "newPassword";
        UserRequest request = new UserRequest("AddNewUserForChangePassword" ,
                oldPassword,
                Authority.USER,
                "AddNewUserForChangePassword@company.com",
                "AddNewUserForChangePassword",
                "AddNewUserForChangePassword");
        UserResponse addedUser = userService.add(request);
        Optional<UserDocument> userDocument = userRepository.findById(addedUser.getId());
        given(this.passwordEncoder.matches(oldPassword, userDocument.get().getPassword())).willReturn(Boolean.TRUE);

        // when
        userService.changePassword(addedUser.getId(), oldPassword, newPassword);

        //then
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

}