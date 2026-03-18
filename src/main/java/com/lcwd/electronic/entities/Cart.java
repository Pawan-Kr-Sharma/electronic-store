package com.lcwd.electronic.entities;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    private String cartItemId;
    private Date createdAt;
    @OneToOne
    @JoinColumn(name = "user_Id")
    private User user;

//mapping cart-item
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
}
