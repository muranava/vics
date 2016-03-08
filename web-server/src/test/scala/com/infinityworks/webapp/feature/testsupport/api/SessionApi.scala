package com.infinityworks.webapp.feature.api

import java.security.Principal

import com.infinityworks.common.lang.Try
import com.infinityworks.webapp.domain.User
import com.infinityworks.webapp.service.{SessionService, UserService}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.springframework.context.ApplicationContext

class SessionApi(context: ApplicationContext) {

  var userSession: SessionService = _

  def withSession(): SessionService = {
    userSession = mock(classOf[SessionService])
    userSession
  }

  def withUser(account: Account): Unit = {
    when(
      userSession.extractUserFromPrincipal(any(classOf[Principal]))
    ).thenReturn(
      Try.success(matchUser(account))
    )
  }

  def matchUser(account: Account): User = account match {
    case BasicUser => covs()
    case Earlsdon => earlsdon()
    case Admin => admin()
  }

  protected def admin(): User = {
    val userService: UserService = context.getBean(classOf[UserService])
    userService.getByEmail("me@admin.uk").get
  }

  protected def covs(): User = {
    val userService: UserService = context.getBean(classOf[UserService])
    userService.getByEmail("cov@south.cov").get
  }

  protected def earlsdon(): User = {
    val userService: UserService = context.getBean(classOf[UserService])
    userService.getByEmail("earlsdon@cov.uk").get
  }
}

sealed abstract class Account

case object BasicUser extends Account

case object Earlsdon extends Account

case object Admin extends Account