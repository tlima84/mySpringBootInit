package com.example.api.core.service.user;

import com.example.api.core.exception.EntityAlreadyExistsException;
import com.example.api.core.exception.EntityNotFoundException;
import com.example.api.core.repository.UserRepository;
import com.example.api.core.repository.model.User;
import com.example.api.resources.user.request.UserRequest;
import com.example.api.resources.user.response.UserResponse;
import com.example.api.core.utils.SlugParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<UserResponse> getUser(String slug, String name) {

        List<User> userList = userRepository
                .findAll(Example.of(User.builder()
                        .slug(slug)
                        .name(name)
                        .build()));

        return userList.stream().map(user -> UserResponse
                .builder()
                .name(user.getName())
                .slug(user.getSlug())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        slugValidation(userRequest.getSlug(), userRequest.getName());

        User user = userRepository.saveAndFlush(User.builder()
                .name(userRequest.getName())
                .slug(StringUtils.isEmpty(userRequest.getSlug()) ?
                        SlugParserUtil.toSlug(userRequest.getName()) :
                        userRequest.getSlug())
                .build());

        return UserResponse.builder()
                .slug(user.getSlug())
                .name(user.getName())
                .build();
    }


    @Override
    public UserResponse updateUser(UserRequest userRequest, String slug) {


        User oldUser = userRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(String.format("User not found for slug='%s'", slug)));

        slugValidation(userRequest.getSlug(), userRequest.getName());

        User newUser = userRepository.saveAndFlush(User.builder().id(oldUser.getId()).slug(StringUtils.isEmpty(userRequest.getSlug()) ?
                SlugParserUtil.toSlug(userRequest.getName()) :
                userRequest.getSlug()).name(userRequest.getName()).build());

        return UserResponse.builder().name(newUser.getName()).slug(newUser.getSlug()).build();
    }

    @Override
    public void deleteUser(String userSlug) {
        User user = userRepository.findBySlug(userSlug).orElseThrow(() -> new EntityNotFoundException(String.format("User not found for slug='%s'", userSlug)));
        userRepository.delete(user);
    }

    private void slugValidation(String slug, String name) {
        String findSlug = StringUtils.isEmpty(slug) ? SlugParserUtil.toSlug(name) : slug;
        if(userRepository.findBySlug(findSlug).isPresent()){
            throw new EntityAlreadyExistsException(String.format("User with slug='%s' already exists", findSlug));
        }
    }
}
