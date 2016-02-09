package com.infinityworks.webapp.service;

import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.repository.ConstituencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstituencyService {
    private final ConstituencyRepository constituencyRepository;

    @Autowired
    public ConstituencyService(ConstituencyRepository constituencyRepository) {
        this.constituencyRepository = constituencyRepository;
    }

    public List<Constituency> getAll() {
        return constituencyRepository.findAll();
    }
}
