package school.hei.spring_srp_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.spring_srp_project.dto.StockResponse;
import school.hei.spring_srp_project.entity.Ingredient;
import school.hei.spring_srp_project.service.IngredientService;

import java.sql.SQLException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService service;
    public IngredientController(IngredientService service){ this.service = service; }

    @GetMapping
    public List<Ingredient> all() throws SQLException { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable int id) throws SQLException {
        Ingredient ing = service.getById(id);
        if (ing == null) return ResponseEntity.status(404).body("Ingredient.id=" + id + " is not found");
        return ResponseEntity.ok(ing);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> stock(@PathVariable int id,
                                   @RequestParam(required = false) String unit,
                                   @RequestParam(required = false) String at) throws SQLException {
        if(unit == null || at == null)
            return ResponseEntity.status(400)
                    .body("Either mandatory query parameter `at` or `unit` is not provided.");

        Ingredient ing = service.getById(id);
        if (ing == null) return ResponseEntity.status(404).body("Ingredient.id=" + id + " is not found");

        Instant instant;
        try { instant = Instant.parse(at); }
        catch(DateTimeParseException e) {
            return ResponseEntity.status(400)
                    .body("Query parameter `at` must be in ISO-8601 format, e.g. 2026-03-27T14:00:00Z");
        }

        double value = service.getStockAt(id, instant);
        return ResponseEntity.ok(new StockResponse(unit, value));
    }
}