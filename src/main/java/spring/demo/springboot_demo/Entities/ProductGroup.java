package spring.demo.springboot_demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PRODUCT_GROUPS")
public class ProductGroup {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "GROUP_NAME")
    private String groupName;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created;

    @OneToMany(mappedBy = "productGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<GroupVariant> groupVariants;
}
