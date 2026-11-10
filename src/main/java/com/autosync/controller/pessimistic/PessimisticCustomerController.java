package com.autosync.controller.pessimistic;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.autosync.model.pessimistic.PessimisticCustomer;
import com.autosync.service.pessimistic.PessimisticCustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessimistic/customers")
public class PessimisticCustomerController {

    private final PessimisticCustomerService customerService;

    public PessimisticCustomerController(PessimisticCustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<PessimisticCustomer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public PessimisticCustomer getCustomersById(@PathVariable long id) {
        return customerService.getAllCustomers(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found."));
    }

    @PostMapping
    public PessimisticCustomer createCustomer(@Valid @RequestBody PessimisticCustomer customer) {
        return customerService.createCustomer(customer);
    }

}
