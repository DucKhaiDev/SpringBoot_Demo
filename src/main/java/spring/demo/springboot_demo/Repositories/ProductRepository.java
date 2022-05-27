package spring.demo.springboot_demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.demo.springboot_demo.Entities.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {
}
