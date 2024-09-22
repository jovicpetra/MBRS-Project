package com.example.demo.services.impl;

import com.example.demo.models.Client;

import com.example.demo.repositories.ClientRepository;
import com.example.demo.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findOne(Long id) {
        Optional<Client> clientResponse = clientRepository.findById(id);
        return clientResponse.get();
    }

    @Override
    public Client save(Client client) { return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        Optional<Client> currentClientResponse = clientRepository.findById(client.getId());
        Client currentClient = currentClientResponse.get();
        if (currentClient == null)
            return null;

        return clientRepository.save(currentClient);
    }
    @Override
    public void remove(Long id) {
        clientRepository.deleteById(id);
    }
}





