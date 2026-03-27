package school.hei.spring_srp_project.repository;


import org.springframework.stereotype.Repository;
import school.hei.spring_srp_project.entity.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private final DataSource ds;

    public ProductRepository(DataSource ds) { this.ds = ds; }

    public List<Product> findAll(int page, int size) throws SQLException {
        String sql = "SELECT * FROM product ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Product(rs.getInt("id"), rs.getString("name"),
                            rs.getDouble("price"), rs.getTimestamp("creation_datetime").toInstant()));
                }
                return list;
            }
        }
    }
}
