package spring.demo.springboot_demo.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockModel {
    public String productName = "";
    public String variantName = "";
    public String stock = "0";
}
