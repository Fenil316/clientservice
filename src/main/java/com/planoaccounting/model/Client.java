package com.planoaccounting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.planoaccounting.PlanoAccountingConstants;
import com.planoaccounting.validation.FrameworkError;
import com.planoaccounting.validation.HasCustomValidations;
import com.planoaccounting.validation.RequestValidator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Client implements HasCustomValidations<Client> {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cliendId;

    @Enumerated(value = EnumType.STRING)
    private ClientType clientType;

    @NotNull(message = "SSN is required")
    private String ssn;

    @NotNull(message = "Profile information is required")
    @Valid
    @JsonManagedReference
    @OneToOne(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Profile profile;

    @Valid
    @JsonManagedReference
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Finance> finance = new ArrayList<>();

    public Client() {
    }

    public Client(ClientType clientType, String ssn, Profile profile) {
        this.clientType = clientType;
        this.ssn = ssn;
        this.profile = profile;
    }

    public Long getCliendId() {
        return cliendId;
    }

    public void setCliendId(Long cliendId) {
        this.cliendId = cliendId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Collection<Finance> getFinance() {
        return finance;
    }

    public void setFinance(Collection<Finance> finance) {
        this.finance = finance;
    }

    @Override
    public List<FrameworkError> isValid(Client request, List<FrameworkError> errorsList) {
        List<FrameworkError> errors = new ArrayList<>();
        if(!errorsList.isEmpty()) errors.addAll(errorsList);

        if(request.getFinance() != null) {
            request.getFinance().forEach(finance -> RequestValidator.validRequest(finance));
            String dup = duplicateFinancesTaxIds(request.getFinance());
            if(dup != null && !dup.isEmpty()) {
                FrameworkError error = new FrameworkError(PlanoAccountingConstants.DUP, "Duplicate tax Id: " + dup);
                errors.add(error);
            }

        }

        return errors;
    }

    private String duplicateFinancesTaxIds(Collection<Finance> finance) {
        Set<String> set = new HashSet<>();
        return finance.stream().map(fin -> fin.getTaxId()).filter(i->!set.add(i)).findFirst().get();
    }

}
