package com.infinityworks.webapp.feature

import com.infinityworks.webapp.common.RequestValidator
import com.infinityworks.webapp.error.RestErrorHandler
import com.infinityworks.webapp.feature.api.{Admin, BasicUser, Http, SessionApi}
import com.infinityworks.webapp.feature.testsupport.StringContainsIgnoreCase.containsStringIgnoringCase
import com.infinityworks.webapp.rest.{UserController, WardController}
import com.infinityworks.webapp.service._
import org.hamcrest.collection.IsCollectionWithSize._
import org.hamcrest.core.Is._
import org.junit.{Before, Test}
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
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
  var http: Http = _

  @Before
  def setup() = {
    session = new SessionApi(applicationContext)
    sessionService = session.withSession()

    val wardService: WardService = getBean(classOf[WardService])
    val wardAssociationService: WardAssociationService = getBean(classOf[WardAssociationService])
    val addressService: AddressService = getBean(classOf[AddressService])
    val wardController: WardController = new WardController(sessionService, wardService, wardAssociationService, new RestErrorHandler, addressService)
    val userController: UserController = new UserController(getBean(classOf[UserService]), getBean(classOf[RestErrorHandler]), getBean(classOf[UserDetailsService]), sessionService, getBean(classOf[AuthenticationManager]), getBean(classOf[RequestValidator]))
    mockMvc = MockMvcBuilders.standaloneSetup(wardController, userController).build
    http = new Http(mockMvc)
    pafStub.start()
  }

  @Test
  def checksUserHasAssociations(): Unit = {
    session withUser BasicUser

    (http GET "/ward/test")
      .andExpect(status.isOk)
      .andExpect(jsonPath("hasAssociation", is(true)))
  }

  @Test
  def returnsTheWardsSummariesForCovs(): Unit = {
    session withUser BasicUser

    (http GET "/ward?summary=true")
      .andExpect(status.isOk)
      .andExpect(jsonPath("$[0].name", is("Binley and Willenhall")))
      .andExpect(jsonPath("$[0].code", is("E05001219")))
      .andExpect(jsonPath("$[0].constituencyName", is("Coventry South")))
  }

  @Test
  def returnsStreetsByWard(): Unit = {
    session withUser BasicUser
    val wardCode = "E05001221"
    pafStub willReturnStreetsByWard wardCode

    (http GET s"/ward/$wardCode/street")
      .andExpect(status.isOk)
      .andExpect(jsonPath("$[0].mainStreet", is("Kirby Road")))
      .andExpect(jsonPath("$[0].postTown", is("Coventry")))
      .andExpect(jsonPath("$[0].dependentLocality", is("Northern Quarter")))
      .andExpect(jsonPath("$[0].dependentStreet", is("")))
  }

  @Test
  def returnsTheWardsByName(): Unit = {
    session withUser Admin
    val name = "wood"
    val limit = 2
    val endpoint = s"/ward/search?limit=$limit&name=$name"

    (http GET endpoint)
      .andExpect(status.isOk)
      .andExpect(jsonPath("$", hasSize(limit)))
      .andExpect(jsonPath("$[0].name", containsStringIgnoringCase(name)))
      .andExpect(jsonPath("$[1].name", containsStringIgnoringCase(name)))
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
      .andExpect(status.isOk)
      .andExpect(jsonPath("$.wards", hasSize(6)))
      .andExpect(jsonPath("$.wards[0].code", is("E05001219")))
      .andExpect(jsonPath("$.wards[0].name", is("Binley and Willenhall")))
      .andExpect(jsonPath("$.wards[0].constituency.name", is("Coventry South")))
  }

  @Test
  def returnsFailureIfNotAdminWhenSearchingByName(): Unit = {
    session withUser BasicUser

    (http GET "/ward/search?limit=2&name=wood")
      .andExpect(status.isUnauthorized)
  }
}
