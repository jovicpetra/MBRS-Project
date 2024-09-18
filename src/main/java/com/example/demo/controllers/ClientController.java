package com.example.demo.controllers;


import com.example.demo.models.Client;
import com.example.demo.models.dto.ClientDTO;
import com.example.demo.services.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/api/client")
public class ClientController {
    @Autowired
    private
    ClientService clientService;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<List<ClientDTO>> getAll() {
        List<Client> clientList = clientService.findAll();
        if (clientList == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<ClientDTO> clientDTOList = new ArrayList<>();

        for (Client client : clientList) {
            ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
            clientDTOList.add(clientDTO);
        }

        return new ResponseEntity<>(clientDTOList, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ResponseEntity<ClientDTO> appointmentService(@PathVariable Long id) {
        Client client = clientService.findOne(id);
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(modelMapper.map(client, ClientDTO.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ClientDTO> create(@RequestBody @Valid ClientDTO newGame) {

        Client createdAppointment = clientService.save(modelMapper.map(newGame,Client.class));
        if (createdAppointment == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(createdAppointment, ClientDTO.class), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<ClientDTO> updateById(@RequestBody @Valid ClientDTO client,
                                                     @PathVariable Long id) {
        if (id != client.getId())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Client updated = clientService.update(modelMapper.map(client,Client.class));
        if (updated == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(updated, ClientDTO.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE) ResponseEntity<String> deleteById(@PathVariable Long id) {

        clientService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
