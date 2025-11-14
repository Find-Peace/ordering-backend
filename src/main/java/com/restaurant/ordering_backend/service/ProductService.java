package com.restaurant.ordering_backend.service;

import com.restaurant.ordering_backend.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * 菜品 (Product) 模块的业务逻辑服务接口。
 * (菜品“大厨”的“菜谱”)
 */

public interface ProductService  {

    /**
     * 获取所有菜品，或根据分类 ID 筛选
     * @param categoryId (可选) 分类 ID
     * @return 菜品列表
     */
    List<Product> getAllProducts(Long categoryId);

    /**
     * 根据 ID 查找菜品
     * @param id 菜品 ID
     * @return Optional 包装的菜品对象
     */
    Optional<Product> getProductById(Long id);

    /**
     * 创建或更新一个菜品
     * (未来可以在此添加“检查库存”、“关联促销”等逻辑)
     * @param product 要保存的菜品对象
     * @return 保存后的菜品对象 (包含 ID)
     */
    Product saveProduct(Product product);

    /**
     * 根据 ID 下架一个菜品
     * @param id 要下架的菜品 ID
     * @return 下架后的菜品对象 (包含 ID)
     */
    Optional<Product> softDeleteProduct(Long id);

    /**
     * 检查一个菜品是否存在
     * @param id 菜品 ID
     * @return true 如果存在, 否则 false
     */
    boolean productExists(Long id);
}