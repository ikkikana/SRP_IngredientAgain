package school.hei.spring_srp_project.service;




import org.springframework.stereotype.Service;
import school.hei.spring_srp_project.entity.Ingredient;
import school.hei.spring_srp_project.repository.IngredientRepository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository repo;
    public IngredientService(IngredientRepository repo){ this.repo = repo; }

    public List<Ingredient> getAll() throws SQLException { return repo.findAll(); }
    public Ingredient getById(int id) throws SQLException { return repo.findById(id); }
    public double getStockAt(int id, Instant at) throws SQLException { return repo.getStockValueAt(id, at); }
}