package spring.demo.springboot_demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;
import redis.clients.jedis.Jedis;
import spring.demo.springboot_demo.Cache.Cache;
import spring.demo.springboot_demo.Cache.RedisCache;
import spring.demo.springboot_demo.Storage.StorageService;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
@Getter
public class SpringBootDemoApplication {
    private EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

    @Bean
    public SessionFactory sessionFactory() {
        return this.entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Bean
    public Validator productValidator() {
        return null;
    }

    @Bean
    public Validator groupValidator() {
        return null;
    }

    @Bean
    public Validator orderValidator() {
        return null;
    }

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    @Bean
    public Jedis redisCliFactory() {
        Jedis jedis = new Jedis(redisHost, redisPort);
        jedis.auth(redisPassword);
        return jedis;
    }

    @Bean
    @Autowired
    public Cache cacheObject(ObjectMapper objectMapper) {
        return new RedisCache(objectMapper, redisCliFactory());
    }
}
