package com.autosync.controller.optimistic;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.autosync.model.optimistic.OptimisticCustomer;
import com.autosync.service.optimistic.OptimisticCustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("optimistic/customers")
public class OptimisticCustomerController {

    private final OptimisticCustomerService customerService;

    public OptimisticCustomerController(OptimisticCustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<OptimisticCustomer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public OptimisticCustomer getCustomersById(@PathVariable long id) {
        return customerService.getAllCustomers(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found."));
    }

    @PostMapping
    public OptimisticCustomer createCustomer(@Valid @RequestBody OptimisticCustomer customer) {
        return customerService.createCustomer(customer);
    }

}
