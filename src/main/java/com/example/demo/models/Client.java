package com.example.demo.models;


import javax.persistence.*;

@Entity
public class Client {
    @Id @GeneratedValue
    private long  id;
    @Column(name="firstName" , length = 50)
    private String firstName;

    @Column(name="lastName" , length = 50)
    private String lastName;

    @Column(name="email" , length = 50, unique = true)
    private String email;


    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }

    public void setId(long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }

}
