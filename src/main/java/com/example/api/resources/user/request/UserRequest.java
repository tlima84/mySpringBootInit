package com.example.api.resources.user.request;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String slug;
    private String name;
}
