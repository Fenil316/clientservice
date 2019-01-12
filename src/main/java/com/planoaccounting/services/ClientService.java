package com.planoaccounting.services;

import com.planoaccounting.model.Client;
import com.planoaccounting.model.EndResponse;

import java.util.List;

public interface ClientService {

    public Client addClient(Client client);
    public List<Client> getClients(String clientType);
}
