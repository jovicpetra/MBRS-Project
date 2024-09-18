package com.example.demo.services;
import com.example.demo.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();
    Client findOne(Long id);
    Client save(Client game);
    Client update(Client game);
    void remove(Long id);
}
