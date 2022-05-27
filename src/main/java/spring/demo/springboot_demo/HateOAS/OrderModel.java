package spring.demo.springboot_demo.HateOAS;

import com.fasterxml.jackson.annotation.JsonProperty;
import spring.demo.springboot_demo.Entities.Order;
import spring.demo.springboot_demo.Entities.OrderItem;

import java.util.List;
import java.util.Optional;

public class OrderModel {
    @JsonProperty
    public long id;
    public String name;
    public String address;
    public String city;
    public String zip;
    public String status;
    public String comment;
    public String totalPrice;
    public String type;
    public List<OrderItem> items;

    public OrderModel(Optional<Order> optional) {
        optional.ifPresent(this::setOrder);
    }

    public OrderModel(Order order) {
        setOrder(order);
    }

    private void setOrder(Order order) {
        this.name = order.getName();
        this.address = order.getAddress();
        this.city = order.getCity();
        this.zip = order.getZip();
        this.status = order.getStatus();
        this.comment = order.getComment();
        this.totalPrice = order.getTotalPrice();
        this.type = order.getType();
        this.items = order.getItems();
    }
}
