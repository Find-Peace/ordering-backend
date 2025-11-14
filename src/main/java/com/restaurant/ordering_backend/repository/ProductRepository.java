package com.restaurant.ordering_backend.repository;

import com.restaurant.ordering_backend.model.Product; // 引入我们上一步定义的“菜品蓝图”
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//菜品管理员
// 注意：这里是 "interface" (接口)，不是 "class" (类)
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Spring Boot 会在“幕后”自动帮我们实现所有功能

    /**
     * Spring Data JPA 的魔术方法
     * 只要方法名遵循 “findBy[属性名]“规则，
     * JPA就会自动生成SQL查询"SELECT * FROM product WHERE category_id = ?"
     * @param categoryId 要查询的分类ID
     * @return 该分类下的所有菜品列表
     */
    List<Product> findByCategoryId(Long categoryId);

    //    查询所有 "available = true" 的菜品
    List<Product> findByAvailable(boolean available);

    //    根据分类 ID 查询，并且 "available = true"
    List<Product> findByCategoryIdAndAvailable(Long categoryId, boolean available);

}