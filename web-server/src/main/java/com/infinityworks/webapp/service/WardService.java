package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Ward;

import java.util.List;

/**
 * Finds all the wards within the given constituency
 */
public interface WardService {
    Try<List<Ward>> findByConstituencyName(String name);
}
