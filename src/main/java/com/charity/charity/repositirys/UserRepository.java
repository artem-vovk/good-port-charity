package com.charity.charity.repositirys;

import com.charity.charity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "user", path = "user") //для рест фул доступа
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);


}
