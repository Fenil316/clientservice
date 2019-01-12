package com.planoaccounting.controllers;

import com.planoaccounting.exceptions.ValidationErrorsException;
import com.planoaccounting.model.Client;
import com.planoaccounting.model.EndResponse;
import com.planoaccounting.services.ClientService;
import com.planoaccounting.validation.RequestValidator;
import com.planoaccounting.validation.FrameworkError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    ClientService service;

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<EndResponse<List<Client>>> getAllCompanies(@RequestParam(value = "type", required = false) String clientType) {
        EndResponse<List<Client>> response = new EndResponse<>();
        response.setPayload(service.getClients(clientType));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public ResponseEntity<EndResponse<Client>> addProjects(@RequestBody Client client) {
        EndResponse<Client> response = new EndResponse<>();
        List<FrameworkError> errors = RequestValidator.validRequest(client);
        if(errors.isEmpty())
            response.setPayload(service.addClient(client));
        else
            throw new ValidationErrorsException(errors);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
