package com.hsx.processor.repository;

import com.hsx.common.model.RegisteredUserDBModel;
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
public interface RegisteredUserRepository extends MongoRepository<RegisteredUserDBModel, Long> {

    @Override
    Optional<RegisteredUserDBModel> findById(Long aLong);

    List<RegisteredUserDBModel> findByFirstName(String firstName);

    @Query("{ 'fistName' : ?0 }")
    Stream<RegisteredUserDBModel> findByCustomQueryAndFirstName(String firstName);
}
