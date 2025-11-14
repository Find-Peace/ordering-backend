package com.restaurant.ordering_backend.service;

import com.restaurant.ordering_backend.model.Product;
import com.restaurant.ordering_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.support.PropertiesBasedNamedQueries;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

/**
 * ProductService 的具体实现类。
 * (菜品“大厨”本人)
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * 业务逻辑：
     * 这是 Service 层价值的体现。Controller 不需要知道这个 "if" 判断的存在。
     * Controller 只管调用 getAllProducts()，
     * Service 负责处理 "是否需要筛选" 的具体逻辑。
     */
    @Override
    public List<Product> getAllProducts(Long categoryId) {
        if (categoryId != null) {
            //业务逻辑 1: 按分类筛选（并且只返回可售的）
            return productRepository.findByCategoryIdAndAvailable(categoryId,true);
        }
        else  {
            //业务逻辑 2: 返回全部（并且只返回可售的）
            return productRepository.findByAvailable(true);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        // (未来可以在此添加“检查菜品名是否重复”等逻辑)
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> softDeleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setAvailable(false);
            return Optional.of(productRepository.save(product));
        }
        return Optional.empty();
    }

    @Override
    public boolean productExists(Long id) {
        return productRepository.existsById(id);
    }

}
