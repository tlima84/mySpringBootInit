package com.example.api.core.repository.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "user_demo")
public class User {

    @Id
    @GeneratedValue(generator = "user_uuid_generator")
    @GenericGenerator(name = "user_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "name")
    private String name;
}
