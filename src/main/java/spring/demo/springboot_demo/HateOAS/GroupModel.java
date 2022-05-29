package spring.demo.springboot_demo.HateOAS;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import spring.demo.springboot_demo.Entities.GroupVariant;
import spring.demo.springboot_demo.Entities.ProductGroup;

import java.util.List;
import java.util.Optional;

public class GroupModel extends RepresentationModel<GroupModel> {
    @JsonProperty
    public long id;
    public String name;
    public String price;
    public List<GroupVariant> variants;

    public GroupModel(Optional<ProductGroup> optional) {
        optional.ifPresent(this::setGroup);
    }

    public GroupModel(ProductGroup productGroup) {
        setGroup(productGroup);
    }

    private void setGroup(ProductGroup productGroup) {
        this.id = productGroup.getId();
        this.name = productGroup.getGroupName();
        this.price = productGroup.getPrice();
        this.variants = productGroup.getGroupVariants();
    }
}
