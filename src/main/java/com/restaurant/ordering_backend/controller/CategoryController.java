package com.restaurant.ordering_backend.controller; // 确保这是您刚创建的包

import com.restaurant.ordering_backend.model.Category; // 引入“菜品蓝图”
import com.restaurant.ordering_backend.service.CategoryService; // 引入“大厨”
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

/**
 * 分类 (Category) 模块的控制器 (Controller)。
 * (这是“前台服务员”)
 * * 职责：
 * 1. 接收 HTTP 请求 (如 /api/categories)。
 * 2. 验证基本参数 (未来可以做)。
 * 3. 将业务逻辑“委托”给 CategoryService (大厨)。
 * 4. 将 Service 的返回结果打包成 HTTP 响应 (如 200 OK, 404 Not Found)。
 */
@RestController
@RequestMapping("/api/categories")//统一路径 /api/categories
public class CategoryController {

    //依赖注入,"前台服务员" 现在依赖 "大厨接口"，而不是 "仓库管理员"
    @Autowired
    private CategoryService categoryService;

    // "服务员" 调用 "大厨"
    /**
     * (R) 查询所有分类
     * @return 分类列表
     */
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * (C) 创建一个新分类
     * @param category 从请求体中传入的分类对象
     * @return 保存后的分类对象 (包含 ID)
     */
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        // (注意：Controller 不再关心 "save" 的具体实现)
        return categoryService.saveCategory(category);
    }

    /**
     * (U) 更新一个分类
     * @param id 要更新的分类 ID (来自 URL 路径)
     * @param categoryDetails 包含新数据的对象 (来自请求体)
     * @return 200 OK (带新数据) 或 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        // 1. "服务员" 先问 "大厨"：这个 ID (id) 的分类存在吗？
        Optional<Category> optionalCategory = categoryService.getCategoryById(id);

        if (optionalCategory.isPresent()) {
            // 2. 如果存在，"服务员" 告诉 "大厨" 去更新它
            Category existingcategory = optionalCategory.get();
            //分类只有一个“name”字段需要更新
            existingcategory.setName(categoryDetails.getName());

            Category updatedCategory = categoryService.saveCategory(existingcategory);
            return ResponseEntity.ok(updatedCategory);
        }
        else  {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * (D) 删除一个分类
     * @param id 要删除的分类 ID (来自 URL 路径)
     * @return 204 No Content (删除成功)
     * 404 Not Found (分类不存在)
     * 409 Conflict (分类正在被菜品使用，无法删除)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        //TODO: 警告！如果分类下还有菜品，直接删除可能会报错。
        //我们暂时先实现简单删除，以后再优化这个逻辑

        // 1. "服务员" 先问 "大厨"：这个 ID (id) 存在吗？
        if(categoryService.categoryExists(id)) {
            // 2. 如果存在，"服务员" 告诉 "大厨" 去删除
            boolean deleted = categoryService.deleteCategory(id);
            if(deleted) {
                // 3. "大厨" 说“删除成功”
                return ResponseEntity.noContent().build();// 204 No Content
            }
            else {
                // 5. "大厨" 说“删除失败，因为被占用了”
                //    (409 Conflict 是“冲突”的完美状态码)
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
            }
        }
        else  {
            return ResponseEntity.notFound().build();
        }
    }
}
