package com.example.api.core.service.user;

import com.example.api.resources.user.request.UserRequest;
import com.example.api.resources.user.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getUser(String slug, String name);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(UserRequest userRequest, String slug);

    void deleteUser(String userSlug);
}
