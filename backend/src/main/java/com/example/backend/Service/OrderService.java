package com.example.backend.Service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.Entity.Menu;
import com.example.backend.Entity.Order;
import com.example.backend.Entity.User;
import com.example.backend.Repository.MenuRepository;
import com.example.backend.Repository.OrderRepository;
import com.example.backend.Repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository,UserRepository userRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
    }
    

    public ResponseEntity<Order> createOrder(@RequestBody Order order,User user,Menu menu) {
        
        // 根據username查找對應的User物件
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isEmpty()) {
            // 如果找不到對應的User物件，返回錯誤響應
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        User foundUser = userOptional.get();
        // 根據menuname查找對應的Menu物件
        Optional<Menu> menuOptional = menuRepository.findByMenuname(menu.getMenuname());
        if (menuOptional.isEmpty()) {
            // 如果找不到對應的Menu物件，返回錯誤響應
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        Menu foundMenu = menuOptional.get();
        
        // 設置user和menu屬性
        order.setUser(foundUser);
        order.setMenu(foundMenu);
        // order.setTotalprice(order.getTotalprice());

        // 保存訂單到數據庫
        Order savedOrder = orderRepository.save(order);

    // 返回成功響應
    return ResponseEntity.ok(savedOrder);
}



    // public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    //     User savedUser = userRepository.save(order.getUser());
    //     order.setUser(savedUser);
    //     Order savedOrder = orderRepository.save(order);
    //     return ResponseEntity.ok(savedOrder);
    // }

    public class OrderDTO {
        private Long id;
        private String username;
        private String menuname;
        private Integer price;
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getMenuname() {
            return menuname;
        }
        public void setMenuname(String menuname) {
            this.menuname = menuname;
        }
        public Integer getPrice() {
            return price;
        }
        public void setPrice(Integer price) {
            this.price = price;
        }
        public OrderDTO(Long id, String username, String menuname, Integer price) {
            this.id = id;
            this.username = username;
            this.menuname = menuname;
            this.price = price;
        }
        
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO(order.getId(), order.getUsername(), order.getMenuname(), order.getPrice());
            orderDTOs.add(orderDTO);
        }
        
        return orderDTOs;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        orderRepository.delete(order);
    }
}
