package spring.demo.springboot_demo.Validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import spring.demo.springboot_demo.Entities.ProductGroup;

public class GroupValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductGroup.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "groupName", "name.required");

        ProductGroup productGroup = (ProductGroup) target;

        if (productGroup.getGroupName().length() < 2) {
            errors.rejectValue("groupName", "name.required");
        }
    }
}
