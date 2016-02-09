package com.infinityworks.webapp.rest;

import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.service.ConstituencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/constituency")
public class ConstituencyController {

    private final ConstituencyService constituencyService;

    @Autowired
    public ConstituencyController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET)
    public List<Constituency> all() {
        return constituencyService.getAll();
    }
}
