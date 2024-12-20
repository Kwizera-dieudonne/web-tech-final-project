package com.furniture.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furniture.domain.Furniture;
import com.furniture.domain.Order;
import com.furniture.domain.User;
import com.furniture.repository.FurnitureRepository;
import com.furniture.repository.OrderRepository;
import com.furniture.repository.UserRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FurnitureRepository furnitureRepository;

    public Order createOrder(Order order) {


        Optional<User> existingUser = userRepository.findById(order.getUser().getUserId());
    if (existingUser.isPresent()) {
        order.setUser(existingUser.get());
    } else {
        throw new IllegalArgumentException("User with ID " + order.getUser().getUserId() + " does not exist.");
    }

    // Fetch furniture list from the database
    List<Furniture> furnitureList = new ArrayList<>();
    for (Furniture furniture : order.getFurnitureList()) {
        furnitureRepository.findById(furniture.getId()).ifPresent(furnitureList::add);
    }
    order.setFurnitureList(furnitureList);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
