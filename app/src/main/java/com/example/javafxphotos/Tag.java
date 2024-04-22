package com.example.javafxphotos;

import java.io.Serializable;

public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    String name, value;
    public Tag(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public String getValue(){
        return value;
    }
}
