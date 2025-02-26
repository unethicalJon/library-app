package com.example.library.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestStatus {

    CREATED ("created"), COMPLETED ("completed");
    public String name;
}

