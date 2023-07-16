package com.lcwd.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank
    @Size(min = 4, message = "Title must be of minimum 4 characters !!")
    private String title;

    @NotBlank(message = "Description required !!")
    //@NotBlank = The annotated element must not be null and must contain at least one non-whitespace character. Accepts CharSequence., // @NotNull = you can leave String blank but, you have to add it in json
    //@NotEmpty = The annotated element must not be null nor empty.
    //@NotNull = The annotated element must not be null. Accepts any type.
    private String description;

    private String coverImage;
}
