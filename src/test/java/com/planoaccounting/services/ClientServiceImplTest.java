package com.planoaccounting.services;

import com.planoaccounting.exceptions.ClientAlreadyExistsException;
import com.planoaccounting.exceptions.ValidationErrorsException;
import com.planoaccounting.model.Address;
import com.planoaccounting.model.Client;
import com.planoaccounting.model.ClientType;
import com.planoaccounting.model.Profile;
import com.planoaccounting.repositories.ClientRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.mockito.Mockito;
import static org.mockito.Matchers.*;

@RunWith(SpringRunner.class)
public class ClientServiceImplTest {

    Client client;

    List<Client> clientList;
    List<Client> clientBusinessList;

    String emailId = "jDoe@planoacc.com";

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @TestConfiguration
    static class ClientServiceImplTestContextConfiguration {
        @Bean
        public ClientService clientService() {
            return new ClientServiceImpl();
        }
    }

    @Before
    public void setUp() {
        Address address1 = new Address("1000 XYZ blvd", "Suite 10", "San Diego", "CA", "92131");
        Address address2 = new Address("1001 XYZ blvd", "Suite 11", "San Diego", "CA", "92131");
        Profile profile1 = new Profile("John", "Doe", address1, new Date(), "123-456-7891", "jDoe@planoacc.com");
        Profile profile2 = new Profile("Jane", "Doe", address2, new Date(), "123-456-7892", "jDoe1@planoacc.com");
        client = new Client(ClientType.BUSINESS, "777777777", profile1);
        Client client2 = new Client(ClientType.INDIVIDUAL, "888888888", profile2);
        clientList = new ArrayList<>();
        clientList.add(client);
        clientList.add(client2);
        clientBusinessList = new ArrayList<>();
        clientBusinessList.add(client);
    }

    @Test
    public void getClientsBUSINESSSuccess(){
        List<Client> result;
        Mockito.when(clientRepository.findByClientType(any(ClientType.class)))
                .thenReturn(clientBusinessList);
        result = clientService.getClients(ClientType.BUSINESS.name());

        Assert.assertEquals(result.size(), clientBusinessList.size());
        Assert.assertEquals(result.get(0).getClientType(), ClientType.BUSINESS);
    }

    @Test(expected = ValidationErrorsException.class)
    public void getClientsJunkClientType(){
        Mockito.when(clientRepository.findByClientType(any(ClientType.class)))
                .thenReturn(clientBusinessList);
        clientService.getClients("junk");
    }

    @Test
    public void getClientsSuccess(){
        List<Client> result;
        Mockito.when(clientRepository.findAll())
                .thenReturn(clientList);
        result = clientService.getClients(null);

        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(result.get(0).getSsn(), clientList.get(0).getSsn());
    }

    @Test
    public void getClientByEmailSuccess() {
        Mockito.when(clientRepository.findByProfileEmailId(emailId))
                .thenReturn(client);

        Assert.assertSame(clientService.getClientByEmail(emailId), client);
    }

    @Test(expected = NoSuchElementException.class)
    public void getClientByEmailNotFound() {

       clientService.getClientByEmail("banana@abc.com");
    }

    @Test
    public void addClientSuccess() {
        Mockito.when(clientRepository.findExistingClient(anyString(), anyString()))
                .thenReturn(null);
        Mockito.when(clientRepository.save(client)).thenReturn(client);

        Assert.assertSame(clientService.addClient(client), client);
    }

    @Test(expected = ClientAlreadyExistsException.class)
    public void addClientExistingClient() {
        Mockito.when(clientRepository.findExistingClient(anyString(), anyString()))
                .thenReturn(client);
        clientService.addClient(client);
    }

}