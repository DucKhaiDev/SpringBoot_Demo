package spring.demo.springboot_demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.demo.springboot_demo.Entities.Order;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT odr FROM Order odr JOIN FETCH odr.items WHERE odr.id = (:id)")
    public Order findOneWithItems(@Param("id") Long id);
}
