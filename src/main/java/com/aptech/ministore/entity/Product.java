package com.aptech.ministore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 128)
    @NotNull
    @Length(min = 5, max = 128)
    private String name;
    private Double price;
    private String description;
    private String image;
    private Boolean status;
    private int quantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    @NaturalId(mutable = true)
    private Categories categories;

}
