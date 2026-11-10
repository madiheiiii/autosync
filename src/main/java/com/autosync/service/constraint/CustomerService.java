package com.autosync.service.constraint;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.autosync.model.constraint.Customer;
import com.autosync.repository.constraint.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getAllCustomers(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
