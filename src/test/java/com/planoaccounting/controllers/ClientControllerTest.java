package com.planoaccounting.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planoaccounting.PlanoAccountingConstants;
import com.planoaccounting.exceptions.ValidationErrorsException;
import com.planoaccounting.model.Address;
import com.planoaccounting.model.Client;
import com.planoaccounting.model.ClientType;
import com.planoaccounting.model.Profile;
import com.planoaccounting.services.ClientService;
import com.planoaccounting.validation.FrameworkError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    ObjectMapper mapper = new ObjectMapper();

    Client client;

    List<Client> clientList;
    List<Client> clientBusinessList;

    @Before
    public void setUp() throws Exception {
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
    public void getAllClientsWithType() throws Exception {
        Mockito.when(clientService.getClients(ArgumentMatchers.anyString())).thenReturn(clientBusinessList);
        mockMvc.perform(get("/clients?type=BUSINESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload", hasSize(1)))
                .andExpect(jsonPath("$.payload[0].ssn", is("777777777")))
                .andExpect(jsonPath("$.payload[0].profile.firstname", is("John")));
    }

    @Test
    public void getAllClientsWithJunkType() throws Exception {
        Mockito.when(clientService.getClients(ArgumentMatchers.anyString())).thenThrow(new ValidationErrorsException(Collections.singletonList(new FrameworkError(PlanoAccountingConstants.CLIENT_TYPE, "Exception"))));
        MockHttpServletResponse response=mockMvc.perform(get("/clients?type=junk"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[0].field", is(PlanoAccountingConstants.CLIENT_TYPE)))
                .andReturn().getResponse();
    }


    @Test
    public void getAllClientsWithOutType() throws Exception {
        Mockito.when(clientService.getClients(ArgumentMatchers.anyString())).thenReturn(clientList);
        MockHttpServletResponse response=mockMvc.perform(get("/clients?type=BUSINESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload", hasSize(2)))
                .andExpect(jsonPath("$.payload[0].ssn", is("777777777")))
                .andExpect(jsonPath("$.payload[0].profile.firstname", is("John")))
                .andExpect(jsonPath("$.payload[1].ssn", is("888888888")))
                .andExpect(jsonPath("$.payload[1].profile.firstname", is("Jane")))
                .andReturn().getResponse();
    }

    @Test
    public void getClientByEmailSuccess() throws Exception{
        Mockito.when(clientService.getClientByEmail(ArgumentMatchers.anyString())).thenReturn(client);
        mockMvc.perform(get("/clients/jDoe@planoacc.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.ssn", is("777777777")))
                .andExpect(jsonPath("$.payload.profile.firstname", is("John")));
    }

    @Test
    public void getClientByEmailNotFound() throws Exception{
        Mockito.when(clientService.getClientByEmail(ArgumentMatchers.anyString())).thenThrow(new NoSuchElementException("Client not found"));
        mockMvc.perform(get("/clients/12345"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is(PlanoAccountingConstants.NSE)));
    }

    @Test
    public void addProjectsSuccess() throws Exception {
        Mockito.when(clientService.addClient(ArgumentMatchers.any())).thenReturn(client);
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.payload.ssn", is("777777777")));
    }

    @Test
    public void addProjectsError() throws Exception {
        Mockito.when(clientService.addClient(ArgumentMatchers.any())).thenReturn(client);
        client.setSsn(null);
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[0].field", is("ssn")));
    }
}