package school.hei.spring_srp_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.spring_srp_project.dto.DishCreateRequest;
import school.hei.spring_srp_project.entity.Dish;
import school.hei.spring_srp_project.entity.Ingredient;
import school.hei.spring_srp_project.service.DishService;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService service;

    public DishController(DishService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAll(
            @RequestParam(required = false) Double priceUnder,
            @RequestParam(required = false) Double priceOver,
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(service.getFiltered(priceOver, priceUnder, name));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<DishCreateRequest> requests) {
        try {
            return ResponseEntity.status(201).body(service.create(requests));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> addIngredients(@PathVariable int id, @RequestBody List<Ingredient> ingredients) {
        try {
            return ResponseEntity.ok(service.addIngredients(id, ingredients));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Dish dish = service.getById(id);
        if (dish == null) return ResponseEntity.status(404).body("Dish.id=" + id + " not found");
        return ResponseEntity.ok(dish);
    }
}