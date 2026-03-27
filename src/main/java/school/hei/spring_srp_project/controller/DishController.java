package school.hei.spring_srp_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.spring_srp_project.dto.UpdateResponse;
import school.hei.spring_srp_project.entity.Dish;
import school.hei.spring_srp_project.service.DishService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService service;

    public DishController(DishService service){ this.service = service; }

    @GetMapping
    public List<Dish> all() throws SQLException {
        return service.getAll();
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateIngredients(@PathVariable int id, @RequestBody List<Integer> ingredientIds) throws SQLException {
        if(ingredientIds == null || ingredientIds.isEmpty())
            return ResponseEntity.status(400).body(new UpdateResponse("Request body must contain ingredient IDs"));

        Dish dish = service.getById(id);
        if(dish == null)
            return ResponseEntity.status(404).body(new UpdateResponse("Dish.id=" + id + " is not found"));

        try {
            service.updateIngredients(id, ingredientIds);
        } catch(SQLException e) {
            return ResponseEntity.status(500).body(new UpdateResponse("Database error: " + e.getMessage()));
        }

        return ResponseEntity.ok(new UpdateResponse("Ingredients updated for Dish.id=" + id));
    }
}