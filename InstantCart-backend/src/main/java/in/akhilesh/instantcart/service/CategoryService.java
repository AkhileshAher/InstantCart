package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.CategoryRequest;
import in.akhilesh.instantcart.dto.CategoryResponse;
import in.akhilesh.instantcart.entity.Category;
import in.akhilesh.instantcart.repository.CategoryRepository;
import in.akhilesh.instantcart.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepo.findAll().stream().map(CategoryResponse::from)
                .toList();
    }

    public CategoryResponse createCategory(@Valid CategoryRequest req) {
        if (categoryRepo.existsByName(req.name())) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setName(req.name());
        category.setImage(req.image());
        return CategoryResponse.from(categoryRepo.save(category));
    }

    public CategoryResponse updateCategory(Long id, @Valid CategoryRequest req) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category Not found"));
        category.setName(req.name());
        category.setImage(req.image());
        return CategoryResponse.from(categoryRepo.save(category));
    }

    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        if (productRepo.existsByCategoryId(id)) {
            throw new RuntimeException("Category still has products");
        }
        categoryRepo.deleteById(id);
    }
}
