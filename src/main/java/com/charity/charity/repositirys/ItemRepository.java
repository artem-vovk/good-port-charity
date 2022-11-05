package com.charity.charity.repositirys;

import com.charity.charity.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAll(Pageable pageable);


    @Query(value = "SELECT * " +
            "FROM item " +
            "JOIN users ON item.author = users.id " +
            "WHERE users.username = :username ORDER BY date DESC", nativeQuery = true)
    List<Item> SQLfindByAuthor(@Param("username") String username);






}
