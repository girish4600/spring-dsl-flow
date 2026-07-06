package com.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter@Getter
public class Message {

    private Long id;
    private String customer;
    private String country;
    private String status;

    public Message() {}

    public Message(Long id, String customer, String country, String status) {
        this.id = id;
        this.customer = customer;
        this.country = country;
        this.status = status;
    }

    // getters setters
}