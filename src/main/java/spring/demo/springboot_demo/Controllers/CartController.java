package spring.demo.springboot_demo.Controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import spring.demo.springboot_demo.Cart.CartItem;
import spring.demo.springboot_demo.Cart.CartService;
import spring.demo.springboot_demo.Entities.Order;
import spring.demo.springboot_demo.HateOAS.OrderModel;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/cart")
@Getter
public class CartController {
    CartService cartService;
    Validator orderValidator;

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setOrderValidator(Validator orderValidator) {
        this.orderValidator = orderValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(orderValidator);
    }

    @PostMapping("/")
    public String create() {
        return cartService.createNewCart();
    }

    @PostMapping("/{id}")
    public String addProduct(@PathVariable("id") String cartId, @RequestBody CartItem cartItem) {
        cartService.addProduct(cartId, cartItem);
        return "OK";
    }

    @GetMapping("/{id}")
    public Set<CartItem> getCartItems(@PathVariable("id") String cartId) {
        return cartService.getItems(cartId);
    }

    @DeleteMapping("/{id}/{product_id}")
    public String removeItem(@PathVariable("id") String cartId, @PathVariable("product_id") String productId) {
        cartService.removeProduct(cartId, productId);
        return "OK";
    }

    @PostMapping("{id}/quantity")
    public String setProductQuantity(@PathVariable("id") String cartId, @RequestBody CartItem cartItem) {
        String productId = Long.toString(cartItem.getProductId());
        cartService.setProductQuantity(cartId, productId, cartItem.getQuantity());
        return "OK";
    }

    @PostMapping("{id}/order")
    public OrderModel createOrder(@PathVariable("id") String cartId, @RequestBody @Valid Order order) {
        if (order == null) {
            System.out.println("Order not in POST");
            return null;
        }

        OrderModel orderResource = new OrderModel(cartService.createOrder(cartId, order));

        Link link = WebMvcLinkBuilder.linkTo(OrderController.class).slash(order.getId()).withSelfRel();
        orderResource.add(link);

        if (orderResource.id < 1) {
            System.out.println("Resource not created");
            return null;
        }

        return orderResource;
    }
}
