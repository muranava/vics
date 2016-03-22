package com.infinityworks.webapp.feature

import com.infinityworks.webapp.error.RestErrorHandler
import com.infinityworks.webapp.feature.testsupport.api.{BasicUser, MockHttp, SessionApi}
import com.infinityworks.webapp.rest.GeoController
import com.infinityworks.webapp.service._
import org.hamcrest.core.Is._
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
class GeoFeatureTest extends ApplicationTest {
  var session: SessionApi = _
  var sessionService: SessionService = _
  var http: MockHttp = _

  @Before
  def setup() = {
    session = new SessionApi(applicationContext)
    sessionService = session.withSession()

    val geoController = new GeoController(getBean(classOf[GeoService]), getBean(classOf[RestErrorHandler]))
    mockMvc = MockMvcBuilders.standaloneSetup(geoController).build
    http = new MockHttp(mockMvc)
  }

  @Test
  def getsTheGeoDataForTheGivenAddress(): Unit = {
    session withUser BasicUser

    (http GET "/geo/addresslookup?q=Brandon+Lane+Coventry+UK")
      .andExpect(status.isOk,
        jsonPath("$.status", is("OK")))
  }
}
