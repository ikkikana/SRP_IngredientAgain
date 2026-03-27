package school.hei.spring_srp_project.service;




import org.springframework.stereotype.Service;
import school.hei.spring_srp_project.entity.Product;
import school.hei.spring_srp_project.repository.ProductRepository;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo){ this.repo = repo; }

    public List<Product> getAll(int page, int size) throws SQLException { return repo.findAll(page, size); }
}
