package com.infinityworks.webapp.feature

import com.infinityworks.webapp.common.RequestValidator
import com.infinityworks.webapp.error.RestErrorHandler
import com.infinityworks.webapp.feature.testsupport.JsonUtil
import com.infinityworks.webapp.feature.testsupport.api.{MockHttp, SessionApi}
import com.infinityworks.webapp.rest.UserController
import com.infinityworks.webapp.rest.dto.ImmutablePasswordResetRequest
import com.infinityworks.webapp.service._
import org.junit.{Before, Test}
import org.springframework.test.context.jdbc.{Sql, SqlGroup}
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
class PasswordResetTest extends ApplicationTest {
  var session: SessionApi = _
  var sessionService: SessionService = _
  var http: MockHttp = _

  @Before
  def setup() = {
    session = new SessionApi(applicationContext)
    sessionService = session.withSession()

    val userController = new UserController(getBean(classOf[UserService]), getBean(classOf[RestErrorHandler]), sessionService, getBean(classOf[RequestValidator]), getBean(classOf[LoginService]), getBean(classOf[PasswordResetService]))
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build
    http = new MockHttp(mockMvc)
    pafStub.start()
  }

  @Test
  def shouldTriggerPasswordResetNotification(): Unit = {
    val request = ImmutablePasswordResetRequest.builder().withUsername("stein.fletcher@voteleave.uk").build()
    (http POST (s"/user/passwordreset", JsonUtil.stringify(request))).andExpect(
      status().isOk
    )
  }

  @Test
  def passwordResetFailsIfUserIsNotRegistered(): Unit = {
    val request = ImmutablePasswordResetRequest.builder().withUsername("i_dont_exist@voteleave.uk").build()
    (http POST (s"/user/passwordreset", JsonUtil.stringify(request))).andExpect(
      status().is4xxClientError()
    )
  }
}
