package com.simple.orm.entity;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "Adres")
public class Address {
    @Column(name = "Miasto")
    String city;
    @Column(name = "Ulica")
    String street;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
