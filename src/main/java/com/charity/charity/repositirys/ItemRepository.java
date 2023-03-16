package com.charity.charity.repositirys;


import com.charity.charity.models.Item;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT * FROM item WHERE " +
            "(item.category = :category OR :category IS NULL) AND " +
            "(item.type = :type OR :type IS NULL) AND " +
            "(item.country = :country OR :country IS NULL) " +
            "ORDER BY date DESC", nativeQuery = true)
    @NotNull
    Page<Item> findAll(@NotNull Pageable pageable, @Param("category") String category, @Param("type") String type, @Param("country") String country);


    @Query(value = "SELECT * " +
            "FROM item " +
            "JOIN users ON item.author = users.id " +
            "WHERE users.username = :username ORDER BY date DESC", nativeQuery = true)
    List<Item> SQLfindByAuthor(@Param("username") String username);



//            "SELECT * " +
//            "FROM item " +
//            "JOIN users ON item.author = users.id " +
//            "WHERE users.username = :username ORDER BY date DESC", nativeQuery = true)
//    @Modifying
//    @Query(value = "DELETE * FROM item " +
//            "JOIN custom_info ON item.item_id = custom_info.item_id " +
//            "WHERE custom_info.item_id = :item_id", nativeQuery = true)
//    void SQLdeleteCustomInfoByItemId(@Param("item_id") String item_id);


//    @Modifying
//    @Query(value = "DELETE * FROM item " +
//            "JOIN custom_info ON item.item_id = custom_info.item_id " +
//            "WHERE custom_info.item_id = :item_id", nativeQuery = true)
//    void SQLdeleteCustomInfoByItemId(@Param("item_id") String item_id);

        @Modifying
    @Query(value = "DELETE * FROM item " +
            "JOIN custom_info ON item.item_id = custom_info.item_id " +
            "WHERE custom_info.item_id = :item_id", nativeQuery = true)
    void SQLdeleteCustomInfoByItemId(@Param("item_id") String item_id);


}
