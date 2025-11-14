package com.restaurant.ordering_backend.service;

import com.restaurant.ordering_backend.model.Category;
import java.util.List;
import java.util.Optional;

/**
 * 分类 (Category) 模块的业务逻辑服务接口。
 * (这是“大厨”需要掌握的“菜谱”)
 */
public interface CategoryService {
    /**
     * 获取所有分类
     * @return 分类列表
     */
    List<Category> getAllCategories();

    /**
     * 根据 ID 查找分类
     * @param id 分类 ID
     * @return Optional 包装的分类对象
     */
    Optional<Category> getCategoryById(Long id);

    /**
     * 创建或更新一个分类
     * @param category 要保存的分类对象
     * @return 保存后的分类对象 (包含 ID)
     */
    Category saveCategory(Category category);

    /**
     * 根据 ID 删除一个分类
     * @param id 要删除的分类 ID
     * @return true 如果删除成功; false 如果分类被菜品占用而无法删除
     */
    boolean deleteCategory(Long id);

    /**
     * 检查一个分类是否存在
     * @param id 分类 ID
     * @return true 如果存在, 否则 false
     */
    boolean categoryExists(Long id);
}
