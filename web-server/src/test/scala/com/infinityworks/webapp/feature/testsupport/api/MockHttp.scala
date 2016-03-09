package com.infinityworks.webapp.feature.testsupport.api

import org.springframework.http.MediaType._
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultHandlers._
import org.springframework.test.web.servlet.{MockMvc, ResultActions, ResultMatcher}

class MockHttp(mockMvc: MockMvc) {

  def GET(url: String): ActionExecutor = {
    new ActionExecutor(mockMvc.perform(
      get(url)
        .accept(APPLICATION_JSON))
        .andDo(print()))
  }
}

class ActionExecutor(resultActions: ResultActions) {
  def andExpect(matchers: ResultMatcher*): Unit = {
    matchers.foreach(matcher => resultActions.andExpect(matcher))
  }
}
