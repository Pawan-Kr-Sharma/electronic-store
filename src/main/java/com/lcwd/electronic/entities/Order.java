package com.lcwd.electronic.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;

    //PENDING, DISPATCHED, DELIVERED
    //enum    we use enum instead of String
    private String orderStatus;

    //NOT-PAID, PAID
    //boolean ,enum    we use boolean/enum instead of String
    //boolean - false => NOT-PAID | true => PAID
    private String paymentStatus;
    private int orderAmount;
    @Column(length = 1000)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;

//mapping user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;          //now go to User Entity for mapping orders

    //mapping orderItems
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems= new ArrayList<>();


}
