package com.autosync.service.pessimistic;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.autosync.model.pessimistic.PessimisticCustomer;
import com.autosync.repository.pessimistic.PessimisticCustomerRepository;

@Service
public class PessimisticCustomerService {
    private final PessimisticCustomerRepository customerRepository;

    public PessimisticCustomerService(PessimisticCustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<PessimisticCustomer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<PessimisticCustomer> getAllCustomers(Long id) {
        return customerRepository.findById(id);
    }

    public PessimisticCustomer createCustomer(PessimisticCustomer customer) {
        return customerRepository.save(customer);
    }

}
