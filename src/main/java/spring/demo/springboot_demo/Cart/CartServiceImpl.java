package spring.demo.springboot_demo.Cart;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.demo.springboot_demo.Cache.Cache;
import spring.demo.springboot_demo.Entities.GroupVariant;
import spring.demo.springboot_demo.Entities.Order;
import spring.demo.springboot_demo.Entities.OrderItem;
import spring.demo.springboot_demo.Entities.Product;
import spring.demo.springboot_demo.Services.EcommerceService;

import java.util.*;

@Getter
@Service
public class CartServiceImpl implements CartService {
    private EcommerceService ecommerceService;
    private Cache cache;

    @Autowired
    public void setEcommerceService(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @Autowired
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public String createNewCart() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void addProduct(String cartId, CartItem cartItem) {
        cache.addItemToList(cartId, cartItem);
    }

    @Override
    public void removeProduct(String cartId, String productId) {
        CartItem itemToRemove = new CartItem();
        itemToRemove.setProductId(Long.parseLong(productId));
        cache.removeItemFromList(cartId, itemToRemove);
    }

    @Override
    public void setProductQuantity(String cartId, String productId, int quantity) {
        List<CartItem> list = (List) cache.getList(cartId, CartItem.class);
        list.forEach(cartItem -> {
            try {
                if (cartItem.getProductId() == Long.parseLong(productId)) {
                    cartItem.setQuantity(quantity);
                    cache.removeItemFromList(cartId, cartItem);
                    cache.addItemToList(cartId, cartItem);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @Override
    public Set<CartItem> getItems(String cartId) {
        return new HashSet<CartItem>((List) cache.getList(cartId, CartItem.class));
    }

    @Override
    public Order createOrder(String cartId, Order order) {
        List<CartItem> cartItems = (List) cache.getList(cartId, CartItem.class);

        order = addCartItemsToOrders(cartItems, order);

        if (order == null) {
            System.out.println("Order not set");
        }

        order = ecommerceService.saveOrder(order);
        cache.removeItem(cartId);
        return order;
    }

    private Order addCartItemsToOrders(List<CartItem> cartItems, Order order) {
        cartItems.forEach(cartItem -> {
            Optional<Product> product = ecommerceService.getProduct(cartItem.getProductId());
            int quantity = cartItem.getQuantity() > 0 ? cartItem.getQuantity() : 1;
            long variantId = cartItem.getVariantId();
            
            for (int i = 0; i < quantity; i++) {
                OrderItem orderItem = new OrderItem();
                product.ifPresent(orderItem::setProduct);
                if (variantId > 0) {
                    GroupVariant variant = new GroupVariant();
                    variant.setId(variantId);
                    orderItem.setGroupVariant(variant);
                }

                orderItem.setOrder(order);
                order.getItems().add(orderItem);
            }
        });

        return order;
    }

    private String generateCartRedisId(String cartId) {
        return "cart#" + cartId + "#items";
    }
}
