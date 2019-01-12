package com.planoaccounting.services;

import com.planoaccounting.PlanoAccountingConstants;
import com.planoaccounting.exceptions.ClientAlreadyExistsException;
import com.planoaccounting.exceptions.ValidationErrorsException;
import com.planoaccounting.model.Client;
import com.planoaccounting.model.ClientType;
import com.planoaccounting.model.EndResponse;
import com.planoaccounting.repositories.ClientRepository;
import com.planoaccounting.validation.FrameworkError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    ClientRepository repo;

    @Override
    public List<Client> getClients(String clientType) {
        List<Client> clients = new ArrayList<>();
        if(clientType !=null && !clientType.isEmpty()) {
            if(Arrays.stream(ClientType.values()).map(Enum::name).collect(Collectors.toList()).contains(clientType.toUpperCase())) {
                clients = repo.findByClientType(ClientType.valueOf(clientType));
            } else {
                throw new ValidationErrorsException(Collections.singletonList(new FrameworkError(PlanoAccountingConstants.CLIENT_TYPE, "Invalid Client Type. Acceptable values: INDIVIDUAL and BUSINESS")));
            }
        } else {
            repo.findAll().iterator().forEachRemaining(clients::add);
        }
        return clients;
    }

    @Override
    public Client getClientById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public Client addClient(Client client) {
        Client existing = repo.findExistingClient(client.getSsn(), client.getProfile().getEmailId());
        if(existing != null)
            throw new ClientAlreadyExistsException("Client already exists");

        return repo.save(client);
    }
}
