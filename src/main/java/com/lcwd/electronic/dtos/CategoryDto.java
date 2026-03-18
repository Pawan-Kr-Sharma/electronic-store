package com.lcwd.electronic.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String categoryId;
    @Size(min = 4,message = "title must be of minimum 4 characters !!")
    private String title;
    @NotBlank(message = "description required")
    private String description;
    private String coverImage;
}
