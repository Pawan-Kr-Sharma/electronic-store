package com.lcwd.electronic.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "Cart id is required !!")
    private String cartId;
    @NotBlank(message = "User id is required !!")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "Address is required !!")
    private String billingAddress;
    @NotBlank(message = "Phone number is required !!")
    private String billingPhone;
    @NotBlank(message = "Billing Name is required !!")
    private String billingName;
}
