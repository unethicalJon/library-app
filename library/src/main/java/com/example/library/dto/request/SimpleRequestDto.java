package com.example.library.dto.request;

import com.example.library.datatype.RequestStatus;
import lombok.Data;

@Data
public class SimpleRequestDto {

    private Long id;

    private String description;

    private Long userId;

    private RequestStatus requestStatus;

}
