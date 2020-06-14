package com.hsx.processor.repository;

import com.hsx.common.model.ac.RegisteredUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Nadith
 */
@Repository
public interface RegisteredUserRepository extends MongoRepository<RegisteredUser, Long> {

    @Override
    Optional<RegisteredUser> findById(Long aLong);

    List<RegisteredUser> findByFirstName(String firstName);

    @Query("{ 'fistName' : ?0 }")
    Stream<RegisteredUser> findByCustomQueryAndFirstName(String firstName);
}
