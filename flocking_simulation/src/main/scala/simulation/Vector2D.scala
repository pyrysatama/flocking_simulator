package simulation

case class Vector2D(x: Double, y: Double) {
  val length = math.hypot(x, y)

  /**
   * distance between two points
   */
  def distance(other: Vector2D) = {
    math.hypot((x-other.x).abs, (y-other.y).abs)
  }

  /**
   * If vectors length is bigger than maxLen, changes length to maxLen
   */
  def limit(maxLen: Double): Vector2D = {
    if(this.length > maxLen) this * (maxLen/this.length) else this
  }
  /**
   * Add two vectors and return their sum vector
   */
  def + (other: Vector2D) = {
    Vector2D(x + other.x, y + other.y)
  }

  def * (other: Double): Vector2D = {
    Vector2D(x*other, y*other)
  }

  def - (other: Vector2D): Vector2D = {
    Vector2D(x - other.x, y - other.y)
  }

  def / (other: Vector2D): Vector2D = {
    Vector2D(x/other.x, y/other.y)
  }

  def / (a: Double): Vector2D = {
    Vector2D(x/a, y/a)
  }

  /**
   * If boid moves out of bounds, it appears from the other side
   */
  def bound(xBound: Int, xBoundMin: Int, shapeWidth: Int, yBound: Int, shapeHeight: Int) = {
    val newX =
      if (x >= xBound+shapeWidth)
        x - (xBound-xBoundMin) - 2* shapeWidth
      else if (x < (xBoundMin-shapeWidth))
        x + (xBound-xBoundMin) + 2* shapeWidth
      else x

    val newY =
      if (y >= yBound+shapeHeight)
        y - yBound - 2* shapeHeight
      else if (y < -shapeHeight)
        y + yBound + 2*shapeHeight
      else y

    if (newX != x || newY != y)
      Vector2D(newX, newY)
    else
      this
  }
}
