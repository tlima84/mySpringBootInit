package com.example.api.resources.user;


import com.example.api.resources.common.response.CollectionResponse;
import com.example.api.resources.user.request.UserRequest;
import com.example.api.resources.user.response.UserResponse;
import com.example.api.core.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedList;
import java.util.List;

@Api
@RestController
@RequestMapping(value = "/v1/users")
public class UserController {

    private static final String USER_V1 = "/v1/users";

    @Autowired
    private UserService userService;

    @ApiOperation(value = " Fetch all users ", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return found users", response = UserResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Invalid data")})
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse<UserResponse>> getUser(String slug, String name){

        return ResponseEntity.ok()
                .body(CollectionResponse.<UserResponse>builder()
                        .result(userService.getUser(slug, name)).build());
    }

    @ApiOperation(value = "Update user",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user updated with success", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Invalid user reference")})
    @RequestMapping(value = "{userSlug}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@RequestBody UserRequest userRequest, @PathVariable("userSlug") String userSlug){

        return ResponseEntity.ok().body(userService.updateUser(userRequest, userSlug));
    }


    @ApiOperation(value = "Create a new user",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful user creation", response = UserResponse.class),
            @ApiResponse(code = 409, message = "User already exists"),
            @ApiResponse(code = 404, message = "Invalid user")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody UserRequest userRequest, UriComponentsBuilder builder){

        UserResponse response = userService.createUser(userRequest);

        UriComponents uriComponents =
                builder.path(USER_V1.concat("/{slug}")).buildAndExpand(response.getSlug());

        return ResponseEntity.created(uriComponents.toUri()).body(response);
    }

    @ApiOperation(value = "Delete a user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful user delete", response = UserResponse.class),
            @ApiResponse(code = 404, message = "User doesn't exists")})
    @RequestMapping(method = RequestMethod.DELETE, value = "{userSlug}")
    public ResponseEntity deleteUser(@PathVariable("userSlug") String userSlug){

        userService.deleteUser(userSlug);

        return ResponseEntity.ok().build();
    }
}

