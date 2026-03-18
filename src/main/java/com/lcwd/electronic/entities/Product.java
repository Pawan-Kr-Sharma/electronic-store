package com.lcwd.electronic.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    private String productId;
    private String title;
    @Column(length = 10000)
    private String description;
    private Date addedDate;
    private int price;
    private int discountedPrice;
    private int quantity;
    private boolean live;
    private boolean stock;
    private String productImageName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "product",cascade = CascadeType.REMOVE)
    private CartItem cartItem;
}
