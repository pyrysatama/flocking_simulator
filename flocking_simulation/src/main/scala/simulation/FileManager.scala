package simulation
import scala.collection.mutable.Buffer
import java.io.FileWriter
import java.io.BufferedWriter
import java.io.IOException
import java.io.FileNotFoundException
import scala.io.Source

object FileManager {

  def writeFile(fileName: String, boids: Buffer[Boid]) = {

    var fileContent = Buffer[String]() // buffer for contents of the file

    fileContent += "BOIDS:" // Add file start

    // Loop through boids and add data to fileContent
    for (boid <- boids) {
      boid match {
        case o: Boid => {
          fileContent += "Boid (Velocity, Location, Accerelation):"
          fileContent += o.velocity.x + "," + o.velocity.y
          fileContent += o.position.x + "," + o.position.y
          fileContent += o.acceleration.x + "," + o.acceleration.y
          fileContent += "-"

        }
        case _ => // Ignore other
      }
    }

    // Drop last "-" and replace it with "ENDOFFILE"
    fileContent = fileContent.dropRight(1)
    fileContent += "ENDOFFILE"

    // Write file content to file.
    try {
      val fileWriter = new FileWriter(fileName)
      val buffWriter = new BufferedWriter(fileWriter)

      try {
        buffWriter.write(fileContent.mkString("\n"))
      } finally {
        buffWriter.close()
      }

    } catch {
      // Exceptions are handled by printing.
      case _: FileNotFoundException => println("File not found")
      case _: IOException => println("IOException")
      case _: Throwable => println("Unexpected error")

    }
  }


  def readFile(file: String): Boids.type = {
    var B = simulation.Boids
    val source = Source.fromFile(file)
    val lines = source.getLines()
    try {
      var currentLine = ""

      while (currentLine != "ENDOFFILE" && lines.hasNext) {

        currentLine = lines.next().trim()
        currentLine match {

          case "BOIDS:" => {
            var continue = true
            currentLine = lines.next()
            while (continue) {

              currentLine match {
                case "Boid (Velocity, Location, Accerelation):" => {
                  currentLine = lines.next()
                  var vel = Vector2D(currentLine.take(currentLine.indexOf(",")).toDouble, currentLine.drop(currentLine.indexOf(",") + 1).toDouble)
                  currentLine = lines.next()
                  var loc = Vector2D(currentLine.take(currentLine.indexOf(",")).toDouble, currentLine.drop(currentLine.indexOf(",") + 1).toDouble)
                  currentLine = lines.next()
                  var acc = Vector2D(currentLine.take(currentLine.indexOf(",")).toDouble, currentLine.drop(currentLine.indexOf(",") + 1).toDouble)
                  B.addBoid(Boid(vel, loc, acc))
                  currentLine = lines.next()
                }
                case "-" => {
                  currentLine = lines.next()
                }
                case "ENDOFFILE" => {
                  continue = false
                }
              }
            }
          }
        }
      }
      // Close source after reading has completed succesfully.
      source.close()
      B
    }
    catch {
      case e: IOException => {
        val Exc = new CorruptedFileException("Reading data failed")
        Exc.initCause(e)
        throw Exc
      }
    }
  }
}
