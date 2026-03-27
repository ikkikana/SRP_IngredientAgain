package school.hei.spring_srp_project.service;




import org.springframework.stereotype.Service;
import school.hei.spring_srp_project.entity.Dish;
import school.hei.spring_srp_project.repository.DishRepository;

import java.sql.SQLException;
import java.util.List;

@Service
public class DishService {
    private final DishRepository repo;
    public DishService(DishRepository repo) { this.repo = repo; }

    public List<Dish> getAll() throws SQLException { return repo.findAll(); }
    public Dish getById(int id) throws SQLException { return repo.findById(id); }
    public void updateIngredients(int dishId, List<Integer> ingredientIds) throws SQLException {
        repo.updateIngredients(dishId, ingredientIds);
    }
}
