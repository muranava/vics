package com.infinityworks.webapp.feature.testsupport

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

object JsonUtil {
  val mapper = new ObjectMapper()
    .registerModules(new JavaTimeModule, new Jdk8Module)
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

  def stringify(obj: Any):String = mapper.writeValueAsString(obj)
}
