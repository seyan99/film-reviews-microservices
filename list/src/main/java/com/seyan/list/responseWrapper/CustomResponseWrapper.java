package com.seyan.list.responseWrapper;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponseWrapper<T> {
    private Integer status;
    private String message;
    private T data;
}

