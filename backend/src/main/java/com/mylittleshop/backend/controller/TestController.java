package com.mylittleshop.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {
    
    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        log.info("테스트 컨트롤러 호출됨");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello from MyLittleShop!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cart-test")
    public ResponseEntity<Map<String, Object>> cartTest() {
        log.info("장바구니 테스트 엔드포인트 호출됨");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart API is working!");
        response.put("cart_items", 0);
        response.put("status", "ready");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/wishlist-test")
    public ResponseEntity<Map<String, Object>> wishlistTest() {
        log.info("위시리스트 테스트 엔드포인트 호출됨");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Wishlist API is working!");
        response.put("wishlist_items", 0);
        response.put("status", "ready");
        
        return ResponseEntity.ok(response);
    }
}
