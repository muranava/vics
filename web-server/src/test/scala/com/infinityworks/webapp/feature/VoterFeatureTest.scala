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
      "classpath:drop-create.sql",
      "classpath:constituencies.sql",
      "classpath:wards.sql",
      "classpath:users.sql",
      "classpath:password-reset-token.sql"
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
    val labelService: LabelService = getBean(classOf[LabelService])
    val tableBuilder: TableBuilder = new TableBuilder(new CanvassTableConfig)
    val documentBuilder: DocumentBuilder = new DocumentBuilder(mock(classOf[LogoRenderer]), new CanvassTableConfig)
    val voterController: VoterController = new VoterController(tableBuilder, documentBuilder, voterService, labelService, requestValidator, recordVotedService, contactService, sessionService, new RestErrorHandler)

    mockMvc = MockMvcBuilders.standaloneSetup(voterController).build
    http = new MockHttp(mockMvc)
    pafStub.start()
  }

  @Test
  def deletesAContact(): Unit = {
    session withUser BasicUser
    val ern = "E05001219-AC-104-0"
    val contactId = "de9e5dc5-ca90-44a0-a4c0-d5796288244c"
    val localId = "de9e5dc5-ca90-44a0-a4c0-d5796288244c"
    pafStub.willDeleteAContactRecordFor(ern, contactId)

    (http DELETE s"/elector/$ern/contact/$contactId/localId/$localId").andExpect(
      status().isOk
    )
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
