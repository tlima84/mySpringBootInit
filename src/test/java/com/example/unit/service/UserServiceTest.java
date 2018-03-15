package com.example.unit.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.example.api.core.exception.EntityAlreadyExistsException;
import com.example.api.core.exception.EntityNotFoundException;
import com.example.api.core.repository.UserRepository;
import com.example.api.core.repository.model.User;
import com.example.api.core.service.user.UserServiceImpl;
import com.example.api.resources.user.request.UserRequest;
import com.example.api.resources.user.response.UserResponse;
import com.example.unit.template.DemoTemplateLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void init() {
        FixtureFactoryLoader.loadTemplates("com.example.unit.template");
    }

    @Test
    public void getUserTest(){

        User user = Fixture.from(User.class).gimme(DemoTemplateLoader.USER);

        List<User> repoResponse = new LinkedList<>();

        repoResponse.add(user);

        when(userRepository.findAll(Example.of(user))).thenReturn(repoResponse);

        List<UserResponse> responseList =  userService.getUser("user-slug", "user-name");

        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertNotNull(responseList.get(0).getSlug());
    }

    @Test
    public void createUserTest(){

        when(userRepository.findBySlug("user-slug")).thenReturn(Optional.empty());
        when(userRepository.saveAndFlush(Fixture.from(User.class).gimme(DemoTemplateLoader.USER)))
                .thenReturn(Fixture.from(User.class).gimme(DemoTemplateLoader.USER_WITH_ID));
        UserResponse response = userService.createUser(Fixture.from(UserRequest.class)
                .gimme(DemoTemplateLoader.USER_CREATE_REQUEST));

        assertNotNull(response);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void createUserWithAlreadyExistingSlugTest(){

        when(userRepository.findBySlug("user-slug")).thenReturn(Optional.of(Fixture.from(User.class).gimme(DemoTemplateLoader.USER)));
        userService.createUser(Fixture.from(UserRequest.class)
                .gimme(DemoTemplateLoader.USER_CREATE_REQUEST));

    }

    @Test
    public void updateUserTest(){

        when(userRepository.findBySlug("old-slug")).thenReturn(Optional.of(Fixture.from(User.class).gimme(DemoTemplateLoader.USER_WITH_ID)));
        when(userRepository.findBySlug("user-slug")).thenReturn(Optional.empty());
        when(userRepository.saveAndFlush(Fixture.from(User.class).gimme(DemoTemplateLoader.USER_WITH_ID)))
                .thenReturn(Fixture.from(User.class).gimme(DemoTemplateLoader.USER_WITH_ID));
        UserResponse response = userService.updateUser(Fixture.from(UserRequest.class)
                .gimme(DemoTemplateLoader.USER_CREATE_REQUEST), "old-slug");

        assertNotNull(response);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void updateUserWithAlreadyExistingSlugTest(){

        when(userRepository.findBySlug("old-slug")).thenReturn(Optional.of(Fixture.from(User.class).gimme(DemoTemplateLoader.USER_WITH_ID)));
        when(userRepository.findBySlug("user-slug")).thenReturn(Optional.of(Fixture.from(User.class).gimme(DemoTemplateLoader.USER_WITH_ID)));
        userService.updateUser(Fixture.from(UserRequest.class)
                .gimme(DemoTemplateLoader.USER_CREATE_REQUEST), "old-slug");

    }

    @Test(expected = EntityNotFoundException.class)
    public void updateUserWithInvalidOldSlugTest(){

        when(userRepository.findBySlug("old-slug")).thenReturn(Optional.empty());
        userService.updateUser(Fixture.from(UserRequest.class)
                .gimme(DemoTemplateLoader.USER_CREATE_REQUEST), "old-slug");

    }

    @Test
    public void deleteUser(){

        when(userRepository.findBySlug("user-slug")).thenReturn(Optional.of(Fixture.from(User.class).gimme(DemoTemplateLoader.USER_WITH_ID)));
        userService.deleteUser("user-slug");
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteInvalidUser(){

        when(userRepository.findBySlug("user-slug")).thenReturn(Optional.empty());
        userService.deleteUser("user-slug");
    }
}
