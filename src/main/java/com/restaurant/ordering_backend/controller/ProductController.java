package com.restaurant.ordering_backend.controller; // 确保这是您刚创建的包

import com.restaurant.ordering_backend.model.Product; // 引入“菜品蓝图”
import com.restaurant.ordering_backend.service.ProductService;//引入“大厨”
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 菜品 (Product) 模块的控制器 (Controller)。
 * (这是“前台服务员”)
 * * 职责：
 * 1. 接收 HTTP 请求 (如 /api/products)。
 * 2. 将业务逻辑“委托”给 ProductService (大厨)。
 * 3. 将 Service 的返回结果打包成 HTTP 响应。
 */
@RestController // 1. 告诉 Spring：这个类是用来接收和响应网页/App请求的
@RequestMapping("/api/products") // 2. 给这个类里的所有接口设置一个“统一前缀”，比如 /api/products
public class ProductController {

    @Autowired // 3. 依赖注入：告诉 Spring，请自动把“菜品大厨”(ProductRepository) 给我
    private ProductService productService;

    /**
     * (R) 查询所有菜品，或者根据“categoryId“ 筛选菜品
     *
     * @param categoryId (可选) URL 查询参数 ?categoryId=...
     * 例如： .../api/products?categoryId=1
     * "required = false"表示categoryId这个参数时可选的，不是必须的。
     * @return 菜品列表
     */
    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false) Long categoryId) {
        return productService.getAllProducts(categoryId);
    }

    /**
     * (C) 创建一个新菜品
     * @param product 从请求体中传入的菜品对象
     * @return 保存后的菜品对象 (包含 ID)
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // (注意：这里我们还没处理 "category": {"id": 1} 的关联逻辑，
        // Service 层暂时还只是简单保存)
        return productService.saveProduct(product);
    }

    /**
     * (U) 更新一个已存在的菜品
     * @param id 要更新的菜品 ID (来自 URL 路径)
     * @param productDetails 包含新数据的对象 (来自请求体)
     * @return 200 OK (带新数据) 或 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {

        // 1. "服务员" 问 "大厨"：这个 ID (id) 的菜品存在吗？
        Optional<Product> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {

            // 3. 如果存在，"服务员" 告诉 "大厨" 去更新它
            Product existingProduct = optionalProduct.get();

            existingProduct.setName(productDetails.getName());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setImageUrl(productDetails.getImageUrl());
            existingProduct.setAvailable(productDetails.getAvailable());

            // (更新分类关联)
            existingProduct.setCategory(productDetails.getCategory());

            Product updatedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(updatedProduct);// 返回 200 OK 和更新后的对象

        }
        else  {
            //5.如果不存在,返回 404 not found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * (D) 软删除（下架）一个菜品
     * 并非真正删除它，而是将其'available'标记为false
     * @param id 要下架的菜品 ID (来自 URL 路径)
     * @return 200 OK （带更新后的对象） 或 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        // "服务员" 只需调用 "大厨" 的 "softDelete" 方法
        return productService.softDeleteProduct(id)
                .map(ResponseEntity::ok)// 如果成功(Optional.isPresent)，返回 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build());// 否则，返回 404
    }


}
