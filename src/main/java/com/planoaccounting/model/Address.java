package com.planoaccounting.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Embeddable
public class Address {

    @NotEmpty(message = "Street1 is required")
    @Length.List({@Length(min = 1, message = "street1 too short"),
            @Length(max = 255, message = "street1 too long")})
    private String street1;
    @NotEmpty(message = "Street2 is required")
    @Length.List({@Length(min = 1, message = "street2 too short"),
            @Length(max = 255, message = "street2 too long")})
    private String street2;
    @NotEmpty(message = "City is required")
    @Length.List({@Length(min = 1, message = "city too short"),
            @Length(max = 50, message = "city too long")})
    private String city;
    @NotEmpty(message = "State is required")
    @Length.List({@Length(min = 1, message = "state too short"),
            @Length(max = 50, message = "state too long")})
    private String state;
    @NotEmpty(message = "Zipcode is required")
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "zipcode should be 5 digit or in xxxxx-xxxx format")
    private String zip;

    public Address(String street1, String street2, String city, String state, String zip) {
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Address() {
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
