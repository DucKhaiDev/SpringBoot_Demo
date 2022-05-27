package spring.demo.springboot_demo.Controllers;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class CoreController {
    protected Link createHateoasLink(long id) {
        return WebMvcLinkBuilder.linkTo(getClass()).slash(id).withSelfRel();
    }
}
