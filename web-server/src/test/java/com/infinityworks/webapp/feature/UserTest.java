package com.infinityworks.webapp.feature;

import com.infinityworks.webapp.common.RequestValidator;
import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Role;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.RestErrorHandler;
import com.infinityworks.webapp.repository.UserRepository;
import com.infinityworks.webapp.rest.UserController;
import com.infinityworks.webapp.rest.dto.CreateUserRequest;
import com.infinityworks.webapp.service.SessionService;
import com.infinityworks.webapp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.infinityworks.webapp.common.Json.objectMapper;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = {
                        "classpath:drop-create.sql",
                        "classpath:constituencies.sql",
                        "classpath:wards.sql",
                        "classpath:users.sql"})
})
public class UserTest extends WebApplicationTest {
    private SessionService sessionService;

    @Before
    public void setup() {
        sessionService = mock(SessionService.class);
        UserController votedController = new UserController(getBean(UserService.class), new RestErrorHandler(),
                getBean(UserDetailsService.class), sessionService, getBean(AuthenticationManager.class), getBean(RequestValidator.class));

        mockMvc = MockMvcBuilders
                .standaloneSetup(votedController)
                .build();
        pafApiStub.start();
    }

    @Test
    public void createsANewUser() throws Exception {
        String endpoint = "/user";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        List<UUID> wardIDs = asList(UUID.fromString("39d75cc3-d7ae-49f6-8be4-ea13218a4907"), UUID.fromString("6e5f7fb6-6aed-4370-8325-0d8cc17e98ac"));
        List<UUID> constituencyIDs = singletonList(UUID.fromString("0d338b99-3d15-44f7-904f-3ebc18a7ab4a"));
        CreateUserRequest request = new CreateUserRequest("email@email.com", "password", "amit", "lakhani", "password", Role.USER, wardIDs, constituencyIDs, true);

        mockMvc.perform(post(endpoint)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username", is("email@email.com")));
    }

    @Test
    public void createUserFailsIfEmailInvalid() throws Exception {
        String endpoint = "/user";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        CreateUserRequest request = new CreateUserRequest("com", "pw", "amy", "neale", "pw", Role.USER, emptyList(), emptyList(), true);

        mockMvc.perform(post(endpoint)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserFailsIfUsernameAlreadyExists() throws Exception {
        String endpoint = "/user";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        CreateUserRequest request = new CreateUserRequest("me@admin.uk", "pw", "peter", "ndlovu", "pw", Role.USER, emptyList(), emptyList(), true);

        mockMvc.perform(post(endpoint)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserFailsIfNonAdminAccount() throws Exception {
        String endpoint = "/user";
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(covs()));

        CreateUserRequest request = new CreateUserRequest("mdde@admin.uk", "pw", "mdo", "ambokani", "pw", Role.USER, emptyList(), emptyList(), true);

        mockMvc.perform(post(endpoint)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deletesAUser() throws Exception {
        UserRepository userRepository = getBean(UserRepository.class);
        User covs = userRepository.findOneByUsername("cov@south.cov").get();
        String endpoint = "/user/" + covs.getId().toString();
        when(sessionService.extractUserFromPrincipal(any(Principal.class)))
                .thenReturn(Try.success(admin()));

        mockMvc.perform(delete(endpoint)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<User> deleted = userRepository.findOneByUsername("cov@south.cov");
        assertThat(deleted.isPresent(), is(false));
    }
}
