package com.TrendHive.TrendHive.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "products")
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = "products.categories_merchant",
        attributeNodes ={@NamedAttributeNode("categories"),@NamedAttributeNode("merchant")})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int price;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB") // For MySQL. Adjust for other DBs.
    private byte[] image;

    private Integer stockQuantity;

    @ManyToMany(cascade = { CascadeType.ALL})
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "merchant_id",nullable = false)
    private Merchant merchant;

//    @ManyToOne
//    @JoinColumn(name = "users_id",nullable = false)
//    private User user;
}
