package com.infinityworks.webapp.feature.api

import org.springframework.http.MediaType._
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultHandlers._
import org.springframework.test.web.servlet.{MockMvc, ResultActions}

class Http(mockMvc: MockMvc) {

  def GET(url: String): ResultActions = {
    mockMvc.perform(
      get(url)
      .accept(APPLICATION_JSON))
      .andDo(print())
  }
}
