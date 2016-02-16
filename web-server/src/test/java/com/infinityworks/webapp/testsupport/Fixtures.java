package com.infinityworks.webapp.testsupport;

import com.infinityworks.webapp.domain.Role;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.testsupport.builder.UserBuilder;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.rest.dto.Street;

import java.util.HashSet;
import java.util.Set;

import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;

public class Fixtures {

    public static User aUser() {
        Set<Ward> wards = new HashSet<>();
        wards.add(ward().withWardName("a").build());
        wards.add(ward().withWardName("b").build());

        User user = new UserBuilder().build();
        user.setUsername("Jon");
        user.setPasswordHash("4325");
        user.setRole(Role.USER);
        user.setWards(wards);
        return user;
    }

    public static Street kirbyRoad() {
        return new Street("Kirby Road", "Coventry", "Northern Quarter", "");
    }

    public static Street abbotRoad() {
        return new Street("Abbot Road", "Coventry", "Southern Quarter", "");
    }
}
