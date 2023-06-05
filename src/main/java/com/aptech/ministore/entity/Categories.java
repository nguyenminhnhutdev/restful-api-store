package com.aptech.ministore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategory;
    @NaturalId(mutable = true)
    private String name;
    @NaturalId(mutable = true)
    private int idParentCate;

    @JsonIgnore
    @OneToMany(mappedBy = "categories")
    private List<Product> product;
}
