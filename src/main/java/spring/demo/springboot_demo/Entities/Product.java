package spring.demo.springboot_demo.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID")
    private ProductGroup productGroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID")
    private User user;

    @Override
    public String toString() {
        return name;
    }
}
