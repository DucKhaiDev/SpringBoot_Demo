package spring.demo.springboot_demo.Controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import spring.demo.springboot_demo.Entities.Order;
import spring.demo.springboot_demo.HateOAS.OrderModel;
import spring.demo.springboot_demo.Services.EcommerceService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@Getter
public class OrderController extends CoreController {
    private EcommerceService ecommerceService;
    Validator orderValidator;

    @Autowired
    public void setEcommerceService(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @Autowired
    public void setOrderValidator(Validator orderValidator) {
        this.orderValidator = orderValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(orderValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<OrderModel> index() {
        List<Order> orders = ecommerceService.getOrders();
        List<OrderModel> out = new ArrayList<>();

        if (orders != null) {
            orders.forEach(order -> {
                OrderModel orderModel = new OrderModel(order);
                orderModel.add(createHateoasLink(order.getId()));
                out.add(orderModel);
            });
        }

        return out;
    }

    @PostMapping
    public Order create(@RequestBody @Valid Order order) {
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }

        return ecommerceService.saveOrder(order);
    }

    @RequestMapping("/{id}")
    public OrderModel view(@PathVariable("id") long id) {
        OrderModel orderModel = new OrderModel(ecommerceService.getOrder(id));
        orderModel.add(createHateoasLink(id));
        return orderModel;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Order edit(@PathVariable("id") long id, @RequestBody @Valid Order order) {
        Optional<Order> optionalUpdatedOrder = ecommerceService.getOrder(id);
        Order updatedOrder = optionalUpdatedOrder.orElse(null);

        if (updatedOrder == null) {
            return null;
        }

        return ecommerceService.saveOrder(updatedOrder);
    }
}
