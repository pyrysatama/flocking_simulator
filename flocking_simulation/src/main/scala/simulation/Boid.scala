package simulation

import java.awt.{Graphics2D,Color}
import java.awt.geom._
import scala.collection.mutable.Buffer

case class Boid(var velocity: Vector2D, var position: Vector2D, var acceleration: Vector2D) {

  var other: Buffer[Boid] = Boids.boids
  var maxForce: Double = 0.3  // maximum length of a vector
  var maxSpeed = 4 //speed of which the flocks are flying
  var radius = 40 //distance how far the boid "sees"
  /**
   * Makes boid to align with other boids nearby by calculating their average velocity and steering towards it
   */
  def alignment = {
  //  var radius = 30
    var v = new Vector2D(0, 0)
    var neighborCount = 0
    for(i <- other.indices) {
      if(this.position.distance(other(i).position)<radius && other(i) != this) {
        v += other(i).velocity
      neighborCount += 1
      }
    }
    if(neighborCount>0) {
      v = v/neighborCount
      v = (v/v.length) * maxSpeed
      v -= this.velocity
    }
    v.limit(maxForce)
  }

  /**
   * Makes boid to steer towards middle of the flock by calculating average position of other nearby boids
   * and steering towards the average position
   */
  def cohesion = {
   // var radius = 30
    var v = new Vector2D(0, 0)
    var neighborCount = 0
    for(i <- other.indices) {
      if(this.position.distance(other(i).position)<radius && this != other(i)) {
        v += other(i).position
        neighborCount += 1
      }
    }
    if(neighborCount>0) {
      v = v/neighborCount
      v -= this.position
      v = (v/v.length) * maxSpeed
      v-=this.velocity
    }
    v.limit(maxForce)
  }

  def separation = {
    //var radius = 30
    var v = new Vector2D(0, 0)
    var neighborCount = 0
    for(i <- other.indices) {
      if(this.position.distance(other(i).position)<radius && this != other(i)) {
        var diff = (position-other(i).position)
        diff = diff/(this.position.distance(other(i).position) * this.position.distance(other(i).position))
        v += diff
        neighborCount += 1
      }
    }
    if(neighborCount>0) {
      v = v / neighborCount
      v = (v/v.length) * maxSpeed
      v -= this.velocity
    }
    v.limit(maxForce)
  }

  /**
   * moves boid to the direction of velocity vector
   */
  def move() = {
    /**
     * Weight for alignment, cohesion and separation. We get them from sliders in Simulation class
     */
    var ali: Double = Simulation.a/10.0
    var sepa: Double = Simulation.s/10.0
    var cohe: Double = Simulation.c/10.0

    /**
     * Acceleration for the boid is a sum of alignment, cohesion and separation vectors
     */
    acceleration = (alignment * (ali)) + (cohesion * (cohe)) + (separation * (sepa))
    position = position + velocity //move boid by adding velocity to boids position
    velocity += acceleration //make next velocity equal to acceleration
    velocity.limit(maxSpeed) //limit velocity to maxSpeed
    acceleration = acceleration * 0 //make acceleration 0 before calculating it again
    /**
     * if boid goes out of bounds, makes it appear on the other side
     */
    position = position.bound(Simulation.width, Simulation.width2-12, 10, Simulation.height, 10)
  }


  def draw(g: Graphics2D) = {
    val oldTransform = g.getTransform()

    g.translate(position.x, position.y)
    g.setColor(Color.WHITE)
    g.fill(new Ellipse2D.Double(7.0, 7.0, 7.0, 7.0))

    g.setTransform(oldTransform)
  }
}
