package com.example.backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Entity.Menu;
import com.example.backend.Entity.Order;
import com.example.backend.Entity.User;
import com.example.backend.Service.OrderService;
import com.example.backend.Service.OrderService.OrderDTO;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public  OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

   
    @PostMapping("/create-order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        User user = new User();
        user.setUsername(orderRequest.getUsername());

        Menu menu = new Menu();
        menu.setMenuname(orderRequest.getMenuname());

        Order order = new Order();
        // order.setTotalprice(orderRequest.getTotalprice());

        ResponseEntity<Order> responseEntity = orderService.createOrder(order, user, menu);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Order createdOrder = responseEntity.getBody();
            OrderResponse orderResponse = new OrderResponse(createdOrder.getUser().getUsername(), createdOrder.getMenu().getMenuname());
            // orderResponse.setTotalprice(createdOrder.getTotalprice());
            return ResponseEntity.ok(orderResponse);
        } else {
            // 處理錯誤狀態碼的情況
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }





    public static class OrderRequest {   //用來存放username 、 menuname ，才可以使用json傳遞
        private String username;
        private String menuname;
        // private Long totalprice;

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

        // public Long getTotalprice() {
        //     return totalprice;
        // }

        // public void setTotalprice(Long totalprice) {
        //     this.totalprice = totalprice;
        // }
    }

    public static class OrderResponse {  //創建一個新的資料傳輸物件（DTO）類別，只包含需要返回的屬性。 
        private String username;
        private String menuname;
        // private Long totalprice;

        public OrderResponse(String username, String menuname) {
            this.username = username;
            this.menuname = menuname;
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
        
        // public Long getTotalprice() {
        //     return totalprice;
        // }

        // public void setTotalprice(Long totalprice) {
        //     this.totalprice = totalprice;
        // }
       
    }


    @GetMapping("/order/{userId}")
    public List<OrderDTO> getOrdersByUserId(@PathVariable("userId") Long userId){
        return orderService.getOrdersByUserId(userId);
    }

    @DeleteMapping("/cancelorder/{orderId}")
    public void cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
    }
}


 // @PostMapping("/create-order")    //使用Param網址列帶入參數，可以成功新增
    // public ResponseEntity<Order> createOrder(@RequestBody Order order, @RequestParam String username, @RequestParam String menuname) {
    //     User user = new User();
    //     user.setUsername(username);

    //     Menu menu = new Menu();
    //     menu.setMenuname(menuname);

    //     return orderService.createOrder(order, user, menu);
    // }

    // @PostMapping("/create-order")   //可以成功新增，但會回傳 user、menu的詳細資料
    // public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
    //     User user = new User();
    //     user.setUsername(orderRequest.getUsername());

    //     Menu menu = new Menu();
    //     menu.setMenuname(orderRequest.getMenuname());

    //     Order order = new Order();
    //     order.setTotalprice(orderRequest.getTotalprice());

    //     return orderService.createOrder(order, user, menu);
    // }