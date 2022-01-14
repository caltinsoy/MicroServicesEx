package com.altinsoy.customer.service;

import com.altinsoy.customer.dao.CustomerRepository;
import com.altinsoy.customer.model.Customer;
import com.altinsoy.customer.model.CustomerRegistrationRequest;
import com.altinsoy.customer.model.FraudCheckResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;


    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder().
                firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        //todo.. validation..


        customerRepository.saveAndFlush(customer);

        //todo: check if fraudster
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://localhost:8086/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );

        if (fraudCheckResponse != null && fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }
    }
}
