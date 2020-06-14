package com.hsx.common.model.ac;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RegisteredUser {

    @Id
    private long id;

    public String firstName;
    public String lastName;
    private String userName;
    private String password;
    private boolean active;
    private String roles;
    private String permissions;

}
