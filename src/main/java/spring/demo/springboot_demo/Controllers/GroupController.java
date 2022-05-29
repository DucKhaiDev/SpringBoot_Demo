package spring.demo.springboot_demo.Controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import spring.demo.springboot_demo.Entities.ProductGroup;
import spring.demo.springboot_demo.HateOAS.GroupModel;
import spring.demo.springboot_demo.Services.EcommerceService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gruop")
@Getter
public class GroupController extends CoreController {
    EcommerceService ecommerceService;
    Validator groupValidator;

    @Autowired
    public void setEcommerceService(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @Autowired
    public void setGroupValidator(Validator groupValidator) {
        this.groupValidator = groupValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(groupValidator);
    }

    @GetMapping
    public List<GroupModel> index() {
        List<ProductGroup> list = ecommerceService.getGroups();

        List<GroupModel> out = new ArrayList<>();

        list.forEach(productGroup -> {
            GroupModel groupModel = new GroupModel(productGroup);
            groupModel.add(createHateoasLink(productGroup.getId()));
            out.add(groupModel);
        });

        return out;
    }

    @GetMapping("/{id}")
    public GroupModel view(@PathVariable("id") long id) {
        GroupModel groupModel = new GroupModel(ecommerceService.getGroup(id));
        groupModel.add(createHateoasLink(id));
        return groupModel;
    }

    @PostMapping("/{id}")
    public ProductGroup edit(@PathVariable(value = "id", required = false) long id, @RequestBody @Valid ProductGroup group) {
        Optional<ProductGroup> optionalProductGroup = ecommerceService.getGroup(id);
        ProductGroup updatedGroup = optionalProductGroup.orElse(null);

        if (updatedGroup == null) {
            return null;
        }

        updatedGroup.setGroupName(group.getGroupName());
        updatedGroup.setPrice(group.getPrice());
        updatedGroup.setGroupVariants(group.getGroupVariants());

        if (updatedGroup.getGroupVariants() != null) {
            updatedGroup.getGroupVariants().forEach(groupVariant -> groupVariant.setProductGroup(updatedGroup));
        }

        return ecommerceService.saveGroup(updatedGroup);
    }

    @PostMapping
    public ProductGroup create(@RequestBody @Valid ProductGroup group) {
        if (group.getGroupVariants() != null) {
            group.getGroupVariants().forEach(groupVariant -> groupVariant.setProductGroup(group));
        }

        return ecommerceService.saveGroup(group);
    }
}
