package school.hei.spring_srp_project.controller;

import org.springframework.web.bind.annotation.*;
import school.hei.spring_srp_project.entity.Product;
import school.hei.spring_srp_project.service.ProductService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){ this.service = service; }

    @GetMapping
    public List<Product> all(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) throws SQLException {
        if(page < 1 || size < 1) page = 1; // validation
        return service.getAll(page, size);
    }
}