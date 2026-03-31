package school.hei.spring_srp_project.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import school.hei.spring_srp_project.entity.Dish;
import school.hei.spring_srp_project.entity.Ingredient;
import school.hei.spring_srp_project.dto.DishCreateRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final JdbcTemplate jdbc;

    public DishService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Dish> getAll() {
        return jdbc.query("SELECT id, name, price FROM dish",
                (rs, rowNum) -> mapDish(rs));
    }

    public List<Dish> getFiltered(Double priceOver, Double priceUnder, String name) {
        List<Dish> all = getAll();
        return all.stream()
                .filter(d -> priceOver == null || d.price() > priceOver)
                .filter(d -> priceUnder == null || d.price() < priceUnder)
                .filter(d -> name == null || d.name().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Dish getById(int id) {
        List<Dish> list = jdbc.query("SELECT id, name, price FROM dish WHERE id=?",
                (rs, rowNum) -> mapDish(rs), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Dish> create(List<DishCreateRequest> requests) {
        return requests.stream().map(req -> {
            Integer count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM dish WHERE name = ?",
                    Integer.class,
                    req.name()
            );
            if (count != null && count > 0) {
                throw new IllegalArgumentException("Dish.name=" + req.name() + " already exists");
            }
            jdbc.update("INSERT INTO dish(name, price) VALUES (?, ?)",
                    req.name(), req.price());
            Integer id = jdbc.queryForObject("SELECT id FROM dish WHERE name=?",
                    Integer.class, req.name());
            return new Dish(id, req.name(), req.price(), List.of());
        }).collect(Collectors.toList());
    }

    public Dish addIngredients(int dishId, List<Ingredient> ingredients) {
        Dish dish = getById(dishId);
        if (dish == null) throw new IllegalArgumentException("Dish.id=" + dishId + " not found");
        return new Dish(dish.id(), dish.name(), dish.price(), ingredients);
    }

    private Dish mapDish(ResultSet rs) throws SQLException {
        return new Dish(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                List.of()
        );
    }
}