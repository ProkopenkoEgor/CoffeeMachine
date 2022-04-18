package com.example.coffee.repository;

import com.example.coffee.model.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu,Long> {

        @Query("SELECT o.typeOfOrder FROM OrderMenu o")
        List<String> findTypeOfOrder();

}
