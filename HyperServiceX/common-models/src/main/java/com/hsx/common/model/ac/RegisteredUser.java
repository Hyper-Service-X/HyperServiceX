package com.hsx.common.model.ac;

import javax.persistence.*;

@Entity
@Table(name = "registeredUser")
public class RegisteredUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public String firstName;
    public String lastName;
    private String userName;
    private String password;
    private boolean active;
    private String roles;
    private String permissions;

}
