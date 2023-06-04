package com.example.backend.Service;

import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.backend.Entity.Menu;
import com.example.backend.Repository.MenuRepository;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;


@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public  MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getMenu(){
        List<Menu> menuList = menuRepository.findAll();
        for (Menu menu : menuList) {
            String imageUrl = "" + menu.getImage();
            try {
                byte[] imageBytes = Files.readAllBytes(Paths.get(imageUrl));
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                menu.setImage(base64Image);
            } catch (IOException e) {
                // Handle exception if image file cannot be read
                e.printStackTrace();
            }
        }
        return menuList;
    }

    // public ResponseEntity<String> createMenu(@RequestBody Menu menu){
        
    //     if (menuRepository.findByMenuname(menu.getMenuname()).isPresent()) {
    //         return ResponseEntity.badRequest().body("MenuName is already exist.");
    //     }

    //     menu.setMenuname(menu.getMenuname());
    //     menu.setDescription(menu.getDescription());
    //     menu.setPrice(menu.getPrice());

    //     Menu savedMenu = menuRepository.save(menu);
        
    //     if (savedMenu != null) {
    //         return ResponseEntity.ok("Menu created successfully.");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create menu.");
    //     }

    // }
    public ResponseEntity<String> createMenu( String menuname, String description, Integer price,MultipartFile imageFile) {
        try {
            // 儲存圖片到指定目錄
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path imagePath = Paths.get("menuimg", fileName);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
    
            // 建立 Menu 物件
            Menu menu = new Menu();
            menu.setMenuname(menuname);
            menu.setDescription(description);
            menu.setPrice(price);
            menu.setImage(imagePath.toString());
            // 儲存 Menu 到資料庫
            Menu savedMenu = menuRepository.save(menu);

            if (savedMenu != null) {
                return ResponseEntity.ok("Menu created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create menu.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process request.");
        }
    }
    
    
}
