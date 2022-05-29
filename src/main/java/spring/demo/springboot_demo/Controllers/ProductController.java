package spring.demo.springboot_demo.Controllers;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.demo.springboot_demo.Entities.Product;
import spring.demo.springboot_demo.Entities.ProductImage;
import spring.demo.springboot_demo.HateOAS.ProductModel;
import spring.demo.springboot_demo.Services.EcommerceService;
import spring.demo.springboot_demo.Storage.StorageService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@Getter
public class ProductController extends CoreController {
    private EcommerceService ecommerceService;
    private StorageService storageService;
    private SessionFactory sessionFactory;
    Validator productValidator;

    @Autowired
    public void setEcommerceService(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    public void setProductValidator(Validator productValidator) {
        this.productValidator = productValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(productValidator);
    }

    @GetMapping
    public List<ProductModel> index() {
        List<Product> res = ecommerceService.getProducts();
        List<ProductModel> output = new ArrayList<>();
        res.forEach(product -> {
            ProductModel productModel = new ProductModel(product);
            productModel.add(createHateoasLink(product.getId()));
            output.add(productModel);
        });

        return output;
    }

    @PostMapping
    public Product create(@RequestBody @Valid Product product) {
        return ecommerceService.saveProduct(product);
    }

    @GetMapping("/{id}")
    public ProductModel view(@PathVariable("id") long id) {
        Optional<Product> optionalProduct = ecommerceService.getProduct(id);
        Product product = optionalProduct.orElse(null);

        if (product == null) {
            return null;
        }

        ProductModel productModel = new ProductModel(product);
        productModel.add(createHateoasLink(product.getId()));

        return productModel;
    }

    @PostMapping("/{id}")
    public Product edit(@PathVariable("id") long id, @RequestBody @Valid Product product) {
        Optional<Product> optionalUpdatedProduct = ecommerceService.getProduct(id);
        Product updatedProduct = optionalUpdatedProduct.orElse(null);

        if (updatedProduct == null) {
            return null;
        }

        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setDescription(product.getDescription());

        return ecommerceService.saveProduct(updatedProduct);
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serverFile(@PathVariable("id") String id) {
        Session session = sessionFactory.openSession();
        ProductImage image = (ProductImage) session.get(ProductImage.class, Long.parseLong(id));
        session.close();

        String path = "product-image/" + image.getProduct().getId() + "/";

        Resource file = storageService.loadAsResource(path + image.getPath());
        String mimeType = "image/png";
        try {
            mimeType = file.getURL().openConnection().getContentType();
        } catch (Exception e) {
            System.out.println("Can't get file mimeType. " + e.getMessage());
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @PostMapping("/{id}/uploadimage")
    public String handleFileUpload(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        String path = "/product-images/" + id;
        String filename = storageService.store(file, path);

        return ecommerceService.addProductImage(id, filename);
    }
}
