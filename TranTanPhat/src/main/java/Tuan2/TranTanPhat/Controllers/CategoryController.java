package Tuan2.TranTanPhat.Controllers;

import Tuan2.TranTanPhat.Entities.Category;
import Tuan2.TranTanPhat.Repositories.ICategoryRepository;
import Tuan2.TranTanPhat.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "Category/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "Category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "Category/edit";
        } else {
            return "redirect:/categories";
        }
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "Category/detail";
        } else {
            return "redirect:/categories";
        }
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            if(categoryService.getCategoryById(id).isPresent())
            {
                categoryService.deleteCategoryById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
            }
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete category as there are books associated with it.");
            return "redirect:/categories";
        }
    }
}
