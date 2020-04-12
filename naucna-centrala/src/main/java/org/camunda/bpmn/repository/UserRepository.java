package org.camunda.bpmn.repository;

import org.camunda.bpmn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    User findOneByUsername(String username);

    @Query("select user from User user where user.firstName = :firstname and user.password = :password")
    User findOneByEmailAndPassword(@Param("firstname") String firstname, @Param("password") String lastname);

}