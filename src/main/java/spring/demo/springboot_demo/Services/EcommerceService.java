package spring.demo.springboot_demo.Services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.demo.springboot_demo.Entities.Order;
import spring.demo.springboot_demo.Entities.Product;
import spring.demo.springboot_demo.Entities.ProductGroup;
import spring.demo.springboot_demo.Entities.ProductImage;
import spring.demo.springboot_demo.Repositories.GroupRepository;
import spring.demo.springboot_demo.Repositories.OrderRepository;
import spring.demo.springboot_demo.Repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Service
public class EcommerceService {
    ProductRepository productRepository;
    GroupRepository groupRepository;
    OrderRepository orderRepository;
    private SessionFactory sessionFactory;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public String addProductImage(final String productId, final String filename) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductImage image = new ProductImage();
        Optional<Product> optional = getProduct(Long.parseLong(productId));
        optional.ifPresent(image::setProduct);

        image.setPath(filename);

        try {
            String res = session.save(image).toString();
            session.getTransaction().commit();
            return res;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return "";
    }

    public List<ProductGroup> getGroups() {
        return groupRepository.findAll();
    }

    public Optional<ProductGroup> getGroup(long id) {
        return groupRepository.findById(id);
    }

    public ProductGroup saveGroup(ProductGroup productGroup) {
        return groupRepository.save(productGroup);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrder(long id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
