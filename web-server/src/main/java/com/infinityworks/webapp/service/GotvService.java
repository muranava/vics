package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.rest.dto.GenerateGotvCardRequest;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
public class GotvService {

    public Try<ByteArrayOutputStream> generateElectorsByStreet(String wardCode,
                                                               User user,
                                                               GenerateGotvCardRequest request) {
        return null;
    }
}
