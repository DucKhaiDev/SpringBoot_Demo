package spring.demo.springboot_demo.HateOAS;

import com.fasterxml.jackson.annotation.JsonProperty;
import spring.demo.springboot_demo.Entities.Product;
import spring.demo.springboot_demo.Entities.ProductGroup;

import java.util.Optional;

public class ProductModel {
    @JsonProperty
    public long id;
    public String name;
    public String price;
    public String description;
    public ProductGroup productGroup;

    public ProductModel(Optional<Product> optional) {
        optional.ifPresent(this::setProduct);
    }

    public ProductModel(Product product) {
        setProduct(product);
    }

    private void setProduct(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.productGroup = product.getProductGroup();
    }
}
