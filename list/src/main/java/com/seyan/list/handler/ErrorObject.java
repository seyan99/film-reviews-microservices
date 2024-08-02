package com.seyan.list.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorObject {
    private Integer status;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;
}
