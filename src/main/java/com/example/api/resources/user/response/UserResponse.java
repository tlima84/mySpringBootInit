package com.example.api.resources.user.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String slug;
    private String name;
}
