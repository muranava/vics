package com.infinityworks.webapp.feature

import com.infinityworks.webapp.common.RequestValidator
import com.infinityworks.webapp.error.RestErrorHandler
import com.infinityworks.webapp.feature.testsupport.JsonUtil
import com.infinityworks.webapp.feature.testsupport.api.{BasicUser, MockHttp, SessionApi}
import com.infinityworks.webapp.rest.UserController
import com.infinityworks.webapp.rest.dto.{ImmutableGeneratePasswordFromTokenRequest, ImmutablePasswordResetRequest, PasswordResetRequest}
import com.infinityworks.webapp.service._
import org.hamcrest.CoreMatchers._
import org.junit.{Before, Test}
import org.springframework.http.MediaType.APPLICATION_JSON
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
      "classpath:users.sql",
      "classpath:password-reset-token.sql"
    ))
))
class UsersFeatureTest extends ApplicationTest {
  var session: SessionApi = _
  var sessionService: SessionService = _
  var http: MockHttp = _

  @Before
  def setup() = {
    session = new SessionApi(applicationContext)
    sessionService = session.withSession()

    val userController = new UserController(getBean(classOf[UserService]), getBean(classOf[RestErrorHandler]), sessionService, getBean(classOf[RequestValidator]), getBean(classOf[LoginService]), getBean(classOf[RequestPasswordResetService]))
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
      .andExpect(jsonPath("$.username", equalTo("me@admin.uk")))
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

  @Test
  def shouldTriggerPasswordResetNotification(): Unit = {
    val request = ImmutablePasswordResetRequest.builder().withUsername("stein.fletcher@voteleave.uk").build()
    (http POST("/user/passwordreset", JsonUtil.stringify(request)))
      .andExpect(
        status().isOk,
        jsonPath("$.username", equalTo("stein.fletcher@voteleave.uk"))
      )
  }

  @Test
  def shouldGenerateANewPasswordFromAResetToken(): Unit = {
    val currentPassword = "password"
    val request = ImmutableGeneratePasswordFromTokenRequest.builder()
      .withUsername("stein.fletcher@voteleave.uk")
      .withToken("4up14eqg06n289crpit7muuvrr50pp1k9s29i476ger")
      .build()

    (http POST("/user/generatepassword", JsonUtil.stringify(request)))
      .andExpect(
        status().isOk,
        jsonPath("$.password", instanceOf(classOf[String])),
        jsonPath("$.password", not(equalTo(currentPassword)))
      )
  }
}
