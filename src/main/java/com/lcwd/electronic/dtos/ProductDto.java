package com.lcwd.electronic.dtos;

import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDto {
    private String productId;
    private String title;
    private String description;
    private Date addedDate;
    private int price;
    private int discountedPrice;
    private int quantity;
    private boolean live;
    private boolean stock;
    private String productImageName;

    private CategoryDto category;
}
