package spring.demo.springboot_demo.Cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private long productId;
    private long variantId;
    private int quantity;

    @Override
    public boolean equals(Object obj) {
        CartItem item = (CartItem) obj;
        return item != null && item.getProductId() == this.getProductId();
    }
}
