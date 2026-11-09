package com.autosync.model.pessimistic;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Entity(name = "PessimisticCustomer")
@Table(name = "customer", schema = "pessimistic")
public class PessimisticCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    public PessimisticCustomer(){}

    public PessimisticCustomer(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Long getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
