package com.lcwd.electronic.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class JwtRequest {

    //private String username; ya email bhi rkh skte hai
    private String email;
    private String password;
}
