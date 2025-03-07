package com.example.library.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    CREATED ("created"), PENDING ("pending"), REFUSED ("refused"), ACCEPTED ("accepted");
    public String name;
}
