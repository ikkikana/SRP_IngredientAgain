package school.hei.spring_srp_project.repository;


import org.springframework.stereotype.Repository;
import school.hei.spring_srp_project.entity.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepository {
    private final DataSource ds;

    public CategoryRepository(DataSource ds) { this.ds = ds; }

    public List<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM category";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Category> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
            return list;
        }
    }
}