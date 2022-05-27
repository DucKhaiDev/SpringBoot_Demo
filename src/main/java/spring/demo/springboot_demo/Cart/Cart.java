package spring.demo.springboot_demo.Cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cart {
    private List<CartItem> items;
}
