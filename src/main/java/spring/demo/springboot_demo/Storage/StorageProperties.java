package spring.demo.springboot_demo.Storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("storage")
@Component
@Getter
@Setter
public class StorageProperties {
    private String location = "uploads";
}
