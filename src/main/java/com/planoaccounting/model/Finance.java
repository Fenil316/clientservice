package com.planoaccounting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class Finance {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long financeId;

    @NotEmpty(message = "organization name is required")
    private String organizationName;

    private Address organizationAddress;

    @NotEmpty(message = "Tax id is required")
    private String taxId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Long getFinanceId() {
        return financeId;
    }

    public void setFinanceId(Long financeId) {
        this.financeId = financeId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Address getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(Address organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
