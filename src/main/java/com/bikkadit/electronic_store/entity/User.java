package com.bikkadit.electronic_store.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private String userId;

    @Column(name="user_name")
    private String name;

    @Column(name="email", unique=true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="about")
    private String about;

    @Column(name="gender")
    private String gender;

    @Column(name="image_name")
    private String imageName;
}
