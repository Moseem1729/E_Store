package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "products")
public class Product {

    @Id
    private String productId;
    private String title;
    private String description;
    private int price;
    private int discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_Id")
    private Category category;

}
