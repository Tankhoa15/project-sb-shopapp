package com.example.shopapp.service;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategory();
    Category updateCategory(long categoryId,CategoryDTO categoryDTO);
    void deleteCategory(long id);
}
