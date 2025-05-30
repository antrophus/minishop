package com.mylittleshop.backend.config;

import com.mylittleshop.backend.model.*;
import com.mylittleshop.backend.service.ProductService;
import com.mylittleshop.backend.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 애플리케이션 시작 시 테스트 데이터를 생성하는 클래스
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ProductCategoryService categoryService;
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 있는지 확인
        if (categoryService.findAll().isEmpty()) {
            initializeCategories();
        }
        
        if (productService.findAll().isEmpty()) {
            initializeProducts();
        }
    }
    
    private void initializeCategories() {
        // 카테고리 생성
        ProductCategory skincare = new ProductCategory("스킨케어");
        ProductCategory haircare = new ProductCategory("헤어케어");
        ProductCategory health = new ProductCategory("건강");
        ProductCategory lifestyle = new ProductCategory("생활");
        
        categoryService.save(skincare);
        categoryService.save(haircare);
        categoryService.save(health);
        categoryService.save(lifestyle);
        
        System.out.println("카테고리 초기 데이터가 생성되었습니다.");
    }
    
    private void initializeProducts() {
        List<ProductCategory> categories = categoryService.findAll();
        ProductCategory skincare = categories.stream()
            .filter(c -> c.getName().equals("스킨케어"))
            .findFirst().orElse(null);
        ProductCategory haircare = categories.stream()
            .filter(c -> c.getName().equals("헤어케어"))
            .findFirst().orElse(null);
        ProductCategory health = categories.stream()
            .filter(c -> c.getName().equals("건강"))
            .findFirst().orElse(null);
        ProductCategory lifestyle = categories.stream()
            .filter(c -> c.getName().equals("생활"))
            .findFirst().orElse(null);
        
        List<Product> products = new ArrayList<>();
        
        // 스킨케어 제품들
        if (skincare != null) {
            products.add(createProduct("하이드레이팅 토너", "피부에 깊은 수분을 공급하는 토너", 
                new BigDecimal("35000"), new BigDecimal("31500"), new BigDecimal("28000"), skincare, "SKC001", true, false, false));
                
            products.add(createProduct("모이스처라이저 크림", "건조한 피부에 깊은 수분을 공급하는 프리미엄 모이스처라이저", 
                new BigDecimal("59000"), new BigDecimal("53100"), new BigDecimal("47200"), skincare, "SKC002", false, true, false));
                
            products.add(createProduct("비타민 C 세럼", "밝고 건강한 피부를 위한 고농축 비타민 C 세럼", 
                new BigDecimal("79000"), new BigDecimal("71100"), new BigDecimal("63200"), skincare, "SKC003", true, false, true));
                
            products.add(createProduct("나이트 크림", "밤사이 피부 재생을 돕는 영양 크림", 
                new BigDecimal("89000"), new BigDecimal("80100"), new BigDecimal("71200"), skincare, "SKC004", false, false, false));
                
            products.add(createProduct("아이 크림", "눈가 주변 전용 안티에이징 크림", 
                new BigDecimal("45000"), new BigDecimal("40500"), new BigDecimal("36000"), skincare, "SKC005", false, true, false));
        }
        
        // 헤어케어 제품들
        if (haircare != null) {
            products.add(createProduct("헤어 에센스", "손상된 모발을 복구하는 고농축 에센스", 
                new BigDecimal("39000"), new BigDecimal("35100"), new BigDecimal("31200"), haircare, "HRC001", false, false, true));
                
            products.add(createProduct("영양 샴푸", "모발과 두피에 영양을 공급하는 샴푸", 
                new BigDecimal("25000"), new BigDecimal("22500"), new BigDecimal("20000"), haircare, "HRC002", true, false, false));
                
            products.add(createProduct("딥 트리트먼트 마스크", "주 1-2회 사용하는 집중 케어 마스크", 
                new BigDecimal("55000"), new BigDecimal("49500"), new BigDecimal("44000"), haircare, "HRC003", false, true, false));
        }
        
        // 건강 제품들
        if (health != null) {
            products.add(createProduct("비타민 C", "면역력 강화를 위한 고함량 비타민 C", 
                new BigDecimal("45000"), new BigDecimal("40500"), new BigDecimal("36000"), health, "HTH001", false, false, false));
                
            products.add(createProduct("프로바이오틱스", "장 건강을 위한 유산균 건강기능식품", 
                new BigDecimal("79000"), new BigDecimal("71100"), new BigDecimal("63200"), health, "HTH002", true, true, false));
                
            products.add(createProduct("오메가3", "심혈관 건강을 위한 고순도 오메가3", 
                new BigDecimal("65000"), new BigDecimal("58500"), new BigDecimal("52000"), health, "HTH003", false, false, true));
        }
        
        // 생활 제품들
        if (lifestyle != null) {
            products.add(createProduct("아로마 디퓨저", "집안을 향기롭게 만드는 초음파 디퓨저", 
                new BigDecimal("89000"), new BigDecimal("80100"), new BigDecimal("71200"), lifestyle, "LST001", false, true, false));
                
            products.add(createProduct("라벤더 캔들", "편안한 휴식을 위한 천연 라벤더 향초", 
                new BigDecimal("29000"), new BigDecimal("26100"), new BigDecimal("23200"), lifestyle, "LST002", true, false, false));
                
            products.add(createProduct("에센셜 오일 세트", "다양한 향의 에센셜 오일 5종 세트", 
                new BigDecimal("120000"), new BigDecimal("108000"), new BigDecimal("96000"), lifestyle, "LST003", false, false, true));
        }
        
        // 제품들 저장
        for (Product product : products) {
            productService.save(product);
        }
        
        System.out.println("상품 초기 데이터 " + products.size() + "개가 생성되었습니다.");
    }
    
    private Product createProduct(String name, String description, BigDecimal gmPrice, 
                                BigDecimal gbmPrice, BigDecimal shopPrice, ProductCategory category, 
                                String sku, boolean featured, boolean bestseller, boolean newArrival) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setGmPrice(gmPrice);
        product.setGbmPrice(gbmPrice);
        product.setShopPrice(shopPrice);
        product.setCostPrice(shopPrice.multiply(new BigDecimal("0.8"))); // 원가는 총판가의 80%로 설정
        product.setStockQuantity(100); // 기본 재고 100개
        product.setStatus(ProductStatus.ACTIVE);
        product.setIsActive(true);
        product.setCategory(category);
        product.setSku(sku);
        product.setBrand("MyLittleShop");
        product.setManufacturer("MyLittleShop Co.");
        product.setMinimumOrderQuantity(1);
        product.setMaximumOrderQuantity(10);
        product.setSubscriptionAvailable(false);
        product.setShippingFee(new BigDecimal("3000")); // 기본 배송비 3000원
        product.setFreeShippingThreshold(new BigDecimal("50000")); // 5만원 이상 무료배송
        product.setCountryOfOrigin("대한민국");
        product.setWeight(new BigDecimal("0.5")); // 기본 무게 0.5kg
        product.setFeatured(featured);
        product.setBestseller(bestseller);
        product.setNewArrival(newArrival);
        
        return product;
    }
}