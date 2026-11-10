package com.autosync.service.optimistic;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.autosync.model.optimistic.OptimisticCustomer;
import com.autosync.repository.optimistic.OptimisticCustomerRepository;

@Service
public class OptimisticCustomerService {
    private final OptimisticCustomerRepository customerRepository;

    public OptimisticCustomerService(OptimisticCustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<OptimisticCustomer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<OptimisticCustomer> getAllCustomers(Long id) {
        return customerRepository.findById(id);
    }

    public OptimisticCustomer createCustomer(OptimisticCustomer customer) {
        return customerRepository.save(customer);
    }

}
