package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Address;
import com.infinityworks.webapp.service.client.PafClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PafAddressService {

    private final PafClient pafClient;

    @Autowired
    public PafAddressService(PafClient pafClient) {
        this.pafClient = pafClient;
    }

    public Try<Address> findAddress() {
        Random r = new Random();
        return Try.of(() -> new Address(String.valueOf(r.nextInt()), null));
    }
}
