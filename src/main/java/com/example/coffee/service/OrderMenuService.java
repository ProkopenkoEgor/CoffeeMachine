package com.example.coffee.service;

import com.example.coffee.model.OrderMenu;
import com.example.coffee.repository.OrderMenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderMenuService {
    private final OrderMenuRepository ordermenuRepository;

    public OrderMenuService(OrderMenuRepository ordermenuRepository) {
        this.ordermenuRepository = ordermenuRepository;
    }

    public OrderMenu findById(Long id) {
        return ordermenuRepository.findById(id).orElse(null);
    }

    public List<OrderMenu> findAll(){
        return ordermenuRepository.findAll();
    }

    public OrderMenu saveOrderMenu(OrderMenu ordermenu){
        return ordermenuRepository.save(ordermenu);
    }

    public void deleteById(Long id){
        ordermenuRepository.deleteById(id);
    }

    public List<String> findTypeOfOrder(){
        return ordermenuRepository.findTypeOfOrder();
    }
}
