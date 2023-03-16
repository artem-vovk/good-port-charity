package com.charity.charity.repositirys;

import com.charity.charity.models.CustomInfo;
import com.charity.charity.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustominfoRepository extends JpaRepository<CustomInfo, Long> {

//    List<CustomInfo> findByItem_id (@Param("item_id") Long id);

}

