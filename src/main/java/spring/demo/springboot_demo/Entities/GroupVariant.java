package spring.demo.springboot_demo.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GROUP_VARIANTS")
public class GroupVariant {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "VARIANT_NAME")
    private String variantName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID")
    @JsonBackReference
    @JsonIgnore
    private ProductGroup productGroup;

    @Override
    public String toString() {
        return variantName;
    }
}
