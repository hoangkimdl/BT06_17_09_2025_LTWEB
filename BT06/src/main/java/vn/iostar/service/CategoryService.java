package vn.iostar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iostar.entity.Category;

import java.util.Optional;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);
    Page<Category> search(String keyword, Pageable pageable);
    Optional<Category> findById(Integer id);
    Category save(Category category);
    void deleteById(Integer id);
}

