package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

/**
  *
  * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class ConversionControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "ConversionController GET" should {

    "return zero or more conversions" in {
      val controller = new ConversionController(stubControllerComponents())
      val conversionList = controller.list().apply(FakeRequest(GET, "/"))

      status(conversionList) mustBe OK
      contentType(conversionList) mustBe Some("application/json")
    }

  }
}
