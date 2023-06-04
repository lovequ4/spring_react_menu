package com.example.backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity(name = "orders") //使用order 會與psql衝突
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="menu_id")
    private Menu menu;

    // private Long totalprice;

    public String getUsername() {
        return user.getUsername();
    }

    public String getMenuname() {
        return menu.getMenuname();
    }

    public Integer getPrice() {
        return menu.getPrice();
    }
}

    // @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // private List<OrderItem> items;