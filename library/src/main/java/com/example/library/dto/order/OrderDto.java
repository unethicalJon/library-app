package com.example.library.dto.order;

import com.example.library.datatype.Status;
import lombok.Data;

@Data
public class OrderDto {

    private Long id;

    private String note;

    private String status;

}
