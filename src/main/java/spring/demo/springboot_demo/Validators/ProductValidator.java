package spring.demo.springboot_demo.Validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import spring.demo.springboot_demo.Entities.Product;

public class ProductValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.required");

        Product product = (Product) target;

        if (product.getProductGroup() == null) {
            errors.rejectValue("group", "group.required");
        }

        if (product.getUser() == null) {
            errors.rejectValue("user", "user.required");
        }
    }
}
