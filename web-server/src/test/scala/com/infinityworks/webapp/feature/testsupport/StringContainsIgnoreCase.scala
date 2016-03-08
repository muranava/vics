package com.infinityworks.webapp.feature.testsupport

import org.hamcrest.Matcher
import org.hamcrest.core.SubstringMatcher

object StringContainsIgnoreCase {
  def containsStringIgnoringCase(substring: String): Matcher[String] = {
    new StringContainsIgnoreCase(substring)
  }
}

class StringContainsIgnoreCase(val other: String) extends SubstringMatcher(other) {
  protected def evalSubstringOf(s: String): Boolean = {
    s.toLowerCase.contains(other.toLowerCase)
  }

  protected def relationship: String = {
    "containing (ignoring case)"
  }
}
