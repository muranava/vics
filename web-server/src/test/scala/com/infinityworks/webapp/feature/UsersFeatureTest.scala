package com.infinityworks.webapp.feature

import com.infinityworks.webapp.common.RequestValidator
import com.infinityworks.webapp.error.RestErrorHandler
import com.infinityworks.webapp.feature.testsupport.api.{BasicUser, MockHttp, SessionApi}
import com.infinityworks.webapp.rest.UserController
import com.infinityworks.webapp.service._
import org.hamcrest.core.Is._
import org.junit.{Before, Test}
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.context.jdbc.{Sql, SqlGroup}
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultHandlers._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SqlGroup(Array(
  new Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    scripts = Array(
      "classpath:drop-create.sql",
      "classpath:constituencies.sql",
      "classpath:wards.sql",
      "classpath:users.sql"))
))
class UsersFeatureTest extends ApplicationTest {
  var session: SessionApi = _
  var sessionService: SessionService = _
  var http: MockHttp = _

  @Before
  def setup() = {
    session = new SessionApi(applicationContext)
    sessionService = session.withSession()

    val userDetailsService = getBean(classOf[UserDetailsService])
    val userController = new UserController(getBean(classOf[UserService]), getBean(classOf[RestErrorHandler]),
      userDetailsService, sessionService, getBean(classOf[AuthenticationManager]), getBean(classOf[RequestValidator]))
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build
    http = new MockHttp(mockMvc)
    pafStub.start()
  }

  @Test
  def retrieveAllUsersFailsIfNotAdmin(): Unit = {
    session withUser BasicUser

    (http GET "/user")
      .andExpect(status.isUnauthorized)
  }

  @Test
  def shouldLoginTheUser(): Unit = {
    val adminCreds = "Basic bWVAYWRtaW4udWs6YWRtaW4="

    mockMvc.perform(
      post("/user/login")
        .header("Authorization", adminCreds)
        .accept(APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.username", is("me@admin.uk")))
  }

  @Test
  def shouldFailLoginIfCredentialsIncorrect(): Unit = {
    val adminCreds = "Basic bWVAYWRtaW4udWs6YXNk"

    mockMvc.perform(
      post("/user/login")
        .header("Authorization", adminCreds)
        .accept(APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isUnauthorized)
  }
}
