package com.infinityworks.webapp.feature

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinityworks.canvass.pafstub.PafServerStub
import com.infinityworks.common.lang.Try
import com.infinityworks.webapp.Application
import com.infinityworks.webapp.clients.email._
import com.infinityworks.webapp.config.Config
import com.infinityworks.webapp.security.SecurityConfig
import org.junit.runner.RunWith
import org.junit.{After, Before}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

@RunWith(classOf[SpringJUnit4ClassRunner])
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Array(
  classOf[Application],
  classOf[SecurityConfig],
  classOf[Config],
  classOf[EmailClientConfig],
  classOf[FeatureTestMocks]
))
abstract class ApplicationTest {
  var mockMvc: MockMvc = _

  val pafStub = new PafServerStub(9002)

  val objectMapper = new ObjectMapper()

  @Autowired
  var applicationContext: WebApplicationContext = _

  def getBean[T](clazz: Class[T]): T = applicationContext.getBean(clazz)

  @Before
  def setUp(): Unit = pafStub.start()

  @After
  def tearDown(): Unit = pafStub.stop()
}

@Configuration
class FeatureTestMocks {
  /**
    * Mock the email client so we don't call out to SendGrid in tests
    */
  @Bean
  def emailClient(): EmailClient = {
    new EmailClient {
      override def sendEmail(content: EmailMessage): Try[EmailResponse] =
        Try.success(ImmutableEmailResponse.builder().withMessage("Success").build())
    }
  }
}
