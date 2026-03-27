package school.hei.spring_srp_project.controller;

import org.springframework.web.bind.annotation.*;
import school.hei.spring_srp_project.entity.Category;
import school.hei.spring_srp_project.repository.CategoryRepository;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo){ this.repo = repo; }

    @GetMapping
    public List<Category> all() throws SQLException { return repo.findAll(); }
}