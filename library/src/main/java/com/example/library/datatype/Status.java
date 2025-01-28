package com.example.library.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    PENDING ("pending"), REFUSED ("refused"), ACCEPTED ("accepted");
    public String name;
}
