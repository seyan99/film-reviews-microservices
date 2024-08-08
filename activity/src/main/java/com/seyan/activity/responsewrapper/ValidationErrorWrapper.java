package com.seyan.activity.responsewrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ValidationErrorWrapper {
    private Integer status;
    private String message;
    private List<String> errors;
}
