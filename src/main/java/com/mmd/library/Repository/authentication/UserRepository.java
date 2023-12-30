package com.mmd.library.Repository.authentication;

import com.mmd.library.entity.authentication.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    User findUserByUsernameWithRole(@Param("username") String username);

}
