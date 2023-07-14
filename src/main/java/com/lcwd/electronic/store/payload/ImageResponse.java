package com.lcwd.electronic.store.payload;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private String imageName;
    private String message;
    private HttpStatus httpStatus;
    private boolean success;
}
