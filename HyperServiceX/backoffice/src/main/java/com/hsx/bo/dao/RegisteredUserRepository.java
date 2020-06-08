package com.hsx.bo.dao;

import com.hsx.common.model.ac.RegisteredUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RegisteredUserRepository extends MongoRepository<RegisteredUser, Long> {


    public Optional<RegisteredUser> findById(long id);

    public Optional<RegisteredUser> findByUserName(String userName);

    public List<RegisteredUser> findByLastName(String lastName);

}