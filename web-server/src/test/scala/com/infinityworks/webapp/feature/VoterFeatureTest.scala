package com.infinityworks.webapp.feature

import com.infinityworks.webapp.common.RequestValidator
import com.infinityworks.webapp.error.RestErrorHandler
import com.infinityworks.webapp.feature.testsupport.JsonUtil
import com.infinityworks.webapp.feature.testsupport.api.{BasicUser, MockHttp, SessionApi}
import com.infinityworks.webapp.pdf.renderer.LogoRenderer
import com.infinityworks.webapp.pdf.{CanvassTableConfig, DocumentBuilder, TableBuilder}
import com.infinityworks.webapp.rest.VoterController
import com.infinityworks.webapp.rest.dto.RecordContactRequest
import com.infinityworks.webapp.service.{RecordContactService, _}
import org.junit.{Before, Test}
import org.mockito.Mockito._
import org.slf4j.LoggerFactory
import org.springframework.test.context.jdbc.{Sql, SqlGroup}
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SqlGroup(Array(
  new Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    scripts = Array(
      "classpath:sql/drop-create.sql",
      "classpath:sql/regions.sql",
      "classpath:sql/constituencies.sql",
      "classpath:sql/wards.sql",
      "classpath:sql/users.sql",
      "classpath:sql/password-reset-token.sql"
    ))
))
class VoterFeatureTest extends ApplicationTest {
  var session: SessionApi = _
  var sessionService: SessionService = _
  var http: MockHttp = _
  val log = LoggerFactory.getLogger(classOf[VoterFeatureTest])

  @Before
  def setup() = {
    session = new SessionApi(applicationContext)
    sessionService = session.withSession()

    val voterService: VoterService = getBean(classOf[VoterService])
    val requestValidator: RequestValidator = getBean(classOf[RequestValidator])
    val recordVotedService: RecordVotedService = getBean(classOf[RecordVotedService])
    val contactService: RecordContactService = getBean(classOf[RecordContactService])
    val tableBuilder: TableBuilder = new TableBuilder(new CanvassTableConfig)
    val documentBuilder: DocumentBuilder = new DocumentBuilder(mock(classOf[LogoRenderer]), new CanvassTableConfig)
    val voterController: VoterController = new VoterController(tableBuilder, documentBuilder, voterService, requestValidator, recordVotedService, contactService, sessionService, new RestErrorHandler)

    mockMvc = MockMvcBuilders.standaloneSetup(voterController).build
    http = new MockHttp(mockMvc)
    pafStub.start()
  }

  @Test
  def recordsAContact(): Unit = {
    session withUser BasicUser
    val ern = "E05001221-PD-123-4"
    val body = new RecordContactRequest(3, 3, false, false, false, false, false, false, false, false, false, "", "")
    pafStub.willCreateANewContactRecord(ern)

    val content: String = JsonUtil.stringify(body)
    log.info(content)

    (http POST (s"/elector/$ern/contact", content))
      .andExpect(status().isOk)
  }
}
