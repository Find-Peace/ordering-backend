package com.restaurant.ordering_backend.service;

import com.restaurant.ordering_backend.model.Category;
import com.restaurant.ordering_backend.repository.CategoryRepository;
import com.restaurant.ordering_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


/**
 * CategoryService 的具体实现类。
 * (这是“大厨”本人，他拿着“仓库管理员”的钥匙)
 *
 * @Service 注解：
 * 告诉 Spring Boot：“这是一个服务类，请帮我管理它 (IOC)。”
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    // "大厨" 需要 "仓库管理员"
    @Autowired
    private CategoryRepository categoryRepository;

    // "大厨" 需要 "菜品仓库管理员"
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category saveCategory(Category category) {
        // (未来可以在这里添加“检查分类名是否重复”的逻辑)
        return categoryRepository.save(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        // (未来会在这里添加复杂逻辑)
        // 1. (业务逻辑) 检查是否有菜品正在使用这个分类
        //    我们使用 ProductRepository 的“魔术”方法
        //    (注意：findByCategoryId 返回的是 "available=true" 的，
        //     我们应该用一个能查找所有的，
        //     但为了简单起见，我们先用这个，假设下架的也不删)
        //    (更正：我们应该在 ProductRepository 中添加一个纯净的 findByCategoryId)
        //    (我们之前在 ProductRepository 中已经有 findByCategoryId 了，完美！)

        // 检查是否有菜品在用这个分类
        if(!productRepository.findByCategoryId(id).isEmpty()) {
            return false;
        }
        // 3. 如果列表为空 (没有菜品在用)，执行删除
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean categoryExists(Long id) {
        return categoryRepository.existsById(id);
    }
}
