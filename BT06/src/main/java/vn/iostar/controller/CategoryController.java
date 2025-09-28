package vn.iostar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iostar.entity.Category;
import vn.iostar.service.CategoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 📂 thư mục upload ảnh
    private static final String UPLOAD_DIR = "uploads/";

    // 🟢 Danh sách + tìm kiếm + phân trang
    @GetMapping({"", "/"})
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage;

        if (keyword != null && !keyword.isEmpty()) {
            categoryPage = categoryService.search(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            categoryPage = categoryService.findAll(pageable);
        }

        model.addAttribute("categories", categoryPage);

        return "category/list";
    }

    // 🟢 Hiển thị form thêm mới
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

    // 🟢 Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Category cate = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category ID:" + id));
        model.addAttribute("category", cate);
        return "category/form";
    }

    @Value("${app.upload-dir}")
    private String uploadDir;

    @PostMapping("/save")
    public String save(@ModelAttribute Category category,
                       @RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                file.transferTo(filePath.toFile());
                category.setImages(fileName);
            }
            categoryService.save(category);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/categories";
    }

    // 🟢 Xoá
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}
