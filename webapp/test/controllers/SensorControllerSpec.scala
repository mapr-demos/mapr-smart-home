package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

/**
  *
  * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class SensorControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "SensorController GET" should {

    "return zero or more sensors" in {
      val controller = new SensorController(stubControllerComponents())
      val sensorsList = controller.list().apply(FakeRequest(GET, "/"))

      status(sensorsList) mustBe OK
      contentType(sensorsList) mustBe Some("application/json")
    }

  }
}
