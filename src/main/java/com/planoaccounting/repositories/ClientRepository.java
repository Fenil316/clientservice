package com.planoaccounting.repositories;

import com.planoaccounting.model.Client;
import com.planoaccounting.model.ClientType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query("select client from Client client where client.ssn = ?1 or client.profile.emailId = ?2")
    Client findExistingClient(String ssn, String emailId);

    List<Client> findByClientType(ClientType clientType);
}
