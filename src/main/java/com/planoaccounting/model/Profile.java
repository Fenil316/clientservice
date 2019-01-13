package com.planoaccounting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class Profile {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @NotEmpty(message = "Firstname is required")
    @Length.List({@Length(min = 1, message = "firstname too short"),
            @Length(max = 255, message = "firstname too long")})
    private String firstname;

    @NotEmpty(message = "Lastname is required")
    @Length.List({@Length(min = 1, message = "lastname too short"),
            @Length(max = 255, message = "lastname too long")})
    private String lastname;

    @Valid
    @NotNull(message = "Address is required")
    private Address address;

    @Temporal(value = TemporalType.DATE)
    private Date dob;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "(?:\\d{3}-){2}\\d{4}", message = "phone number should be in xxx-xxx-xxxx format")
    private String phone;

    @Email
    private String emailId;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Profile(String firstname, String lastname, Address address, Date dob, String phone, String emailId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.dob = dob;
        this.phone = phone;
        this.emailId = emailId;
    }

    public Profile() {
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
