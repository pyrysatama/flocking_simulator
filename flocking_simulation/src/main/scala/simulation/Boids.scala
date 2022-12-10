package simulation

import scala.collection.mutable.Buffer
import java.awt.{Graphics2D}

object Boids {

   var boids = Buffer[Boid]()

  def addBoid(boid: Boid): Unit = {
    boids += boid
  }

  /**
   * moves every boid
   */
  def step() = {
    boids.foreach(_.move())
  }

  /**
   * draws boids to screen
   */
  def draw(g: Graphics2D) = {
    boids foreach (_.draw(g))
  }

}
