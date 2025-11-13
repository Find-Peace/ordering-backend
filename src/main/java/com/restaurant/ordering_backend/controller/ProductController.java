package com.restaurant.ordering_backend.controller; // 确保这是您刚创建的包

import com.restaurant.ordering_backend.model.Product; // 引入“菜品蓝图”
import com.restaurant.ordering_backend.repository.ProductRepository; // 引入“数据管理员”
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 1. 告诉 Spring：这个类是用来接收和响应网页/App请求的
@RequestMapping("/api/products") // 2. 给这个类里的所有接口设置一个“统一前缀”，比如 /api/products
public class ProductController {

    @Autowired // 3. 依赖注入：告诉 Spring，请自动把“数据管理员”(ProductRepository) 给我
    private ProductRepository productRepository;

    // 4. 定义一个 GET 请求接口
    // 当有人访问 "http://服务器IP:8080/api/products" 时，就执行这个方法
    @GetMapping
    public List<Product> getAllProducts() {

        // 5. 使用“数据管理员”从数据库中查询所有(findAll)菜品
        List<Product> productList = productRepository.findAll();

        // 6. 将查询到的菜品列表返回给请求者 (Spring Boot 会自动把它们转成 JSON 格式)
        return productList;
    }
}
