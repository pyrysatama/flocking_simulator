import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.math



class SimTests extends AnyFlatSpec with Matchers {

  val vec1 = simulation.Vector2D(2, 4)
  val vec2 = simulation.Vector2D(5, 8)
  val dist = scala.math.sqrt(scala.math.pow(3, 2)+scala.math.pow(4, 2))

  /**
   * Tests for Vector2D.distance
   */
  withClue("Wrong distance") {
    vec1.distance(vec2) should equal (dist)
  }

  withClue("Wrong distance") {
    vec2.distance(vec1) should equal (dist)
  }

  withClue("Wrong distance") {
    vec2.distance(vec2) should equal (0.0)
  }

  withClue("Wrong distance") {
    false should equal (vec2.distance(vec1)==dist+0.1)
  }

  /**
   * Tests for Vector2D.limit
   */
  withClue("Limit doesn't work properly") {
    vec1.limit(4) should equal (simulation.Vector2D(vec1.x*4/math.sqrt(20), vec1.y*4/math.sqrt(20)))
  }

  withClue("Limit doesn't work properly") {
    vec1.limit(5) should equal (vec1)
  }

}
