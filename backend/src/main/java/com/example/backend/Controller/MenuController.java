package com.example.backend.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Entity.Menu;
import com.example.backend.Service.MenuService;

import jakarta.servlet.annotation.MultipartConfig;

import org.springframework.web.multipart.MultipartFile;


@MultipartConfig
@RestController
@RequestMapping("/api")
@CrossOrigin
public class MenuController {
    
    private final MenuService menuService;

    public  MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menu")
    public List<Menu> getMenu(){
        return menuService.getMenu();
    }

    // @PostMapping("/create-menu")
    // public ResponseEntity<String> createMenu(@RequestBody Menu menu){
    //    return menuService.createMenu(menu);
    // }
    
    @PostMapping("/create-menu")
    public ResponseEntity<String> createMenu(@RequestParam("menuname") String menuname,
                                             @RequestParam("description") String description,
                                             @RequestParam("price") Integer price,
                                             @RequestParam("image") MultipartFile imageFile) {
        return menuService.createMenu( menuname, description, price,imageFile);
    }
    
}
    


