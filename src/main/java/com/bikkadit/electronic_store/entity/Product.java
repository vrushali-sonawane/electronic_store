package com.bikkadit.electronic_store.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    private String productId;
    private String title;
    private String description;
    private Double price;
    private Double discountedPrice;
    private Integer quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_Id")
    private Category category;
}
