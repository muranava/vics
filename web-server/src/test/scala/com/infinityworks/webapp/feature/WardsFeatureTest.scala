package com.infinityworks.webapp.feature

import java.util.Collections._

import com.infinityworks.webapp.common.RequestValidator
import com.infinityworks.webapp.error.RestErrorHandler
import com.infinityworks.webapp.feature.testsupport.StringContainsIgnoreCase.containsStringIgnoringCase
import com.infinityworks.webapp.feature.testsupport.api.{Admin, BasicUser, MockHttp, SessionApi}
import com.infinityworks.webapp.rest.dto.AssociateUserWard
import com.infinityworks.webapp.rest.{UserController, WardController}
import com.infinityworks.webapp.service._
import org.hamcrest.collection.IsCollectionWithSize._
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
class WardsFeatureTest extends ApplicationTest {
  var session: SessionApi = _
  var sessionService: SessionService = _
  var http: MockHttp = _

  @Before
  def setup() = {
    session = new SessionApi(applicationContext)
    sessionService = session.withSession()

    val wardService: WardService = getBean(classOf[WardService])
    val wardAssociationService: WardAssociationService = getBean(classOf[WardAssociationService])
    val addressService: AddressService = getBean(classOf[AddressService])
    val wardController: WardController = new WardController(sessionService, wardService, wardAssociationService, new RestErrorHandler, addressService)
    val userController: UserController = new UserController(getBean(classOf[UserService]), getBean(classOf[RestErrorHandler]), sessionService, getBean(classOf[RequestValidator]), getBean(classOf[LoginService]), getBean(classOf[RequestPasswordResetService]))
    mockMvc = MockMvcBuilders.standaloneSetup(wardController, userController).build
    http = new MockHttp(mockMvc)
    pafStub.start()
  }

  @Test
  def checksUserHasAssociations(): Unit = {
    session withUser BasicUser

    (http GET "/ward/test")
      .andExpect(status.isOk,
        jsonPath("hasAssociation", is(true)))
  }

  @Test
  def associatesAWardWithUserByIds(): Unit = {
    session withUser Admin

    val ward = "6b710adf-6d9f-4745-b441-5b58c85d3a07"

    (http POST s"/ward/$ward/user/63f93970-d065-4fbb-8b9c-941e27ea53dc")
        .andExpect(
          status().isOk,
          jsonPath("$..wards[?(@.name =~ /.*Westwood/i)].id", is(singletonList(ward))))
  }

  @Test
  def associatesAWardWithUserByUsernameAndWardCode(): Unit = {
    session withUser Admin

    val data = new AssociateUserWard("me@admin.uk", "E05001235")
    val content = objectMapper.writeValueAsString(data)

    (http POST ("/ward/associate", content))
      .andExpect(
        status().isOk,
        jsonPath("$..wards[?(@.name =~ /.*Wyken/i)].code", is(singletonList(data.getWardCode))))
  }

  @Test
  def failsToAssociateAWardWithUserIfNotAdmin(): Unit = {
    session withUser BasicUser

    val data = new AssociateUserWard("me@admin.uk", "E05001235")
    val content = objectMapper.writeValueAsString(data)

    (http POST ("/ward/associate", content))
      .andExpect(status().isUnauthorized)
  }

  @Test
  def returnsTheWardsSummariesForCovs(): Unit = {
    session withUser BasicUser

    (http GET "/ward?summary=true")
      .andExpect(
        status.isOk,
        jsonPath("$[0].name", is("Binley and Willenhall")),
        jsonPath("$[0].code", is("E05001219")),
        jsonPath("$[0].constituencyName", is("Coventry South")))
  }

  @Test
  def returnsStreetsByWard(): Unit = {
    session withUser BasicUser
    pafStub willReturnStreetsByWard "E05001221"

    (http GET s"/ward/E05001221/street")
      .andExpect(
        status.isOk,
        jsonPath("$.streets[0].mainStreet", is("Acre Road")),
        jsonPath("$.streets[0].postTown", is("KINGSTON UPON THAMES")),
        jsonPath("$.streets[0].dependentLocality", is("")),
        jsonPath("$.streets[0].dependentStreet", is("")))
  }

  @Test
  def returnsTheWardsByName(): Unit = {
    session withUser Admin
    val name = "wood"
    val limit = 2
    val endpoint = s"/ward/search?limit=$limit&name=$name"

    (http GET endpoint)
      .andExpect(status.isOk,
        jsonPath("$", hasSize(limit)),
        jsonPath("$[0].name", containsStringIgnoringCase(name)),
        jsonPath("$[1].name", containsStringIgnoringCase(name)))
  }

  @Test
  def returns404IfConstituencyNotFound(): Unit = {
    (http GET "/constituency/922268a5-5689-418d-b63d-b21545345f01/ward")
      .andExpect(status.isNotFound)
  }

  @Test
  def returnsTheRestrictedWardsForBasicUser(): Unit = {
    session withUser BasicUser

    (http GET "/ward")
      .andExpect(status.isOk,
        jsonPath("$.wards", hasSize(6)),
        jsonPath("$.wards[0].code", is("E05001219")),
        jsonPath("$.wards[0].name", is("Binley and Willenhall")),
        jsonPath("$.wards[0].constituency.name", is("Coventry South")))
  }

  @Test
  def returnsFailureIfNotAdminWhenSearchingByName(): Unit = {
    session withUser BasicUser

    (http GET "/ward/search?limit=2&name=wood")
      .andExpect(status.isUnauthorized)
  }
}
