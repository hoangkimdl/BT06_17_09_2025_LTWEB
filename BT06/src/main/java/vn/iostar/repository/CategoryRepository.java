package vn.iostar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.iostar.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByCategorynameContainingIgnoreCase(String keyword, Pageable pageable);
}
