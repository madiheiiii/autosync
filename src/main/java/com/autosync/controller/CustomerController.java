package com.autosync.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.autosync.model.Customer;
import com.autosync.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping 
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping ("/{id}")
    public Customer getCustomersById(@PathVariable long id){
        return customerService.getAllCustomers(id) .orElseThrow(() -> new ResponseStatusException
        (HttpStatus.NOT_FOUND, "Customer not found."));
    }

    @PostMapping
    public Customer createCustomer(@Valid @RequestBody Customer customer){
        return customerService.createCustomer(customer);
    }
    
}
