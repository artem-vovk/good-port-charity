package com.charity.charity.repositirys;

import com.charity.charity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);


}
