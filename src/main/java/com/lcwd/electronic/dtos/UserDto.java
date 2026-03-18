package com.lcwd.electronic.dtos;

import com.lcwd.electronic.custumvalidate.ImageNameValid;
import com.lcwd.electronic.entities.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min = 4,max = 20,message = "Invalid name !!")
    @ApiModelProperty(value = "user-name",name = "userName",required = true,notes = "user name of new user !!")
    private String name;
    //@Email(message = "Invalid user email")
    //regexp internet se chipka do
    @Pattern(regexp = "^(.+)@(.+)$",message = "Invalid user email")
    @NotBlank(message = "email is required !!")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @Size(min = 4,max = 6,message = "Invalid gender !!")
    private String gender;
    @NotBlank(message = "write something about yourself !! ")
    private String about;

    //pattern
    //custom validator

    @ImageNameValid
    private String imageName;

    private Set<RoleDto> roles = new HashSet<>();
}
