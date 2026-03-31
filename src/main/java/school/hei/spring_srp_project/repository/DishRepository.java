package school.hei.spring_srp_project.repository;

import org.springframework.stereotype.Repository;
import school.hei.spring_srp_project.entity.Dish;
import school.hei.spring_srp_project.entity.Ingredient;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepository {
    private final DataSource ds;

    public DishRepository(DataSource ds) { this.ds = ds; }

    public List<Dish> findAll() throws SQLException {
        String sql = "SELECT * FROM dish";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Dish> list = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                list.add(new Dish(id, rs.getString("name"), rs.getDouble("price"),
                        findIngredientsForDish(id)));
            }
            return list;
        }
    }

    public Dish findById(int id) throws SQLException {
        String sql = "SELECT * FROM dish WHERE id=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Dish(id, rs.getString("name"), rs.getDouble("price"),
                        findIngredientsForDish(id));
            }
        }
    }

    private List<Ingredient> findIngredientsForDish(int dishId) throws SQLException {
        String sql = """
            SELECT i.id, i.name, i.category, i.price
            FROM dish_ingredient di
            JOIN ingredient i ON i.id = di.ingredient_id
            WHERE di.dish_id=?
        """;
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Ingredient> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Ingredient(rs.getInt("id"), rs.getString("name"),
                            rs.getString("category"), rs.getDouble("price")));
                }
                return list;
            }
        }
    }

    public void updateIngredients(int dishId, List<Integer> ingredientIds) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM dish_ingredient WHERE dish_id=?")) {
                del.setInt(1, dishId);
                del.executeUpdate();
            }
            String insertSQL = "INSERT INTO dish_ingredient(dish_id, ingredient_id, quantity, unit) VALUES(?, ?, 1, 'PCS')";
            try (PreparedStatement ins = conn.prepareStatement(insertSQL)) {
                for (Integer ingId : ingredientIds) {
                    ins.setInt(1, dishId);
                    ins.setInt(2, ingId);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
            conn.commit();
        }
    }
}