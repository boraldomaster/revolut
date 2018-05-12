package com.revolut.test.model.customer;

import com.revolut.test.model.Lockable;
import com.revolut.test.model.Money;

import java.math.BigDecimal;

public class Customer extends Lockable {

    private Integer id;
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
