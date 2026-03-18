package com.lcwd.electronic.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePaymentAndOrderStatusRequest {
    private String paymentStatus;
    private String orderStatus;
}
