package com.ginteq.springbootauth.endpoint.repository;

import org.springframework.data.repository.CrudRepository;
/**
 * Created by bachir on 01/10/2017.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findUsername(String username);
}
