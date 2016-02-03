package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Address;

public interface AddressService {
    Try<Address> findAddress();
}
