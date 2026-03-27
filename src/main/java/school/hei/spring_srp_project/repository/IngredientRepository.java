package school.hei.spring_srp_project.repository;




import org.springframework.stereotype.Repository;
import school.hei.spring_srp_project.entity.Ingredient;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientRepository {
    private final DataSource ds;

    public IngredientRepository(DataSource ds) { this.ds = ds; }

    public List<Ingredient> findAll() throws SQLException {
        String sql = "SELECT * FROM ingredient";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Ingredient> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Ingredient(rs.getInt("id"), rs.getString("name"),
                        rs.getString("category"), rs.getDouble("price")));
            }
            return list;
        }
    }

    public Ingredient findById(int id) throws SQLException {
        String sql = "SELECT * FROM ingredient WHERE id=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Ingredient(rs.getInt("id"), rs.getString("name"),
                        rs.getString("category"), rs.getDouble("price"));
            }
        }
    }

    public double getStockValueAt(int ingredientId, Instant at) throws SQLException {
        String sql = "SELECT type, quantity FROM stock_movement WHERE ingredient_id=? AND movement_datetime<=?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            ps.setTimestamp(2, Timestamp.from(at));
            try (ResultSet rs = ps.executeQuery()) {
                double stock = 0;
                while (rs.next()) {
                    stock += rs.getString("type").equals("IN") ? rs.getDouble("quantity") : -rs.getDouble("quantity");
                }
                return stock;
            }
        }
    }
}