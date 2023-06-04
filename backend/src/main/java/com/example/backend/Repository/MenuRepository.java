package com.example.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Entity.Menu;

public interface MenuRepository extends JpaRepository<Menu,Integer>{
    Optional<Menu> findByMenuname(String menuname);
}
