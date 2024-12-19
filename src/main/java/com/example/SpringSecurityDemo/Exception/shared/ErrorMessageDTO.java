package com.example.SpringSecurityDemo.Exception.shared;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ErrorMessageDTO {
    String message;
    Date timestamp;
    Integer code;
}
