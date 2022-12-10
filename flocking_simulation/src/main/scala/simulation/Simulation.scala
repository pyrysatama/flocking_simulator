package simulation

import scala.swing._
import java.awt.{Color, Graphics2D}
import java.awt.event.ActionListener
import scala.swing.event._
import scala.collection.mutable.Buffer
import scala.swing.event.ButtonClicked
import scala.util.Random


object Simulation extends SimpleSwingApplication {

  //Sliders for the weights of alignment, separation and cohesion
  var sliderAlign: Slider = new Slider{
    min = 0
    max = 100
    value = 13
    minorTickSpacing = 10
    paintTicks = true
    labels = scala.collection.mutable.HashMap(50 -> new Label("Alignment"))
    paintLabels = true
    maximumSize = new Dimension(300, 100)
  }

  var sliderSepa: Slider = new Slider{
    min = 0
    max = 100
    value = 13
    minorTickSpacing = 10
    paintTicks = true
    labels = scala.collection.mutable.HashMap(50 -> new Label("Separation"))
    paintLabels = true
    maximumSize = new Dimension(300, 100)
  }

  var sliderCohe: Slider = new Slider{
    min = 0
    max = 100
    value = 12
    minorTickSpacing = 10
    paintTicks = true
    labels = scala.collection.mutable.HashMap(50 -> new Label("Cohesion"))
    paintLabels = true
    maximumSize = new Dimension(300, 100)
  }

  //Variables to use as weights in Boid class
  var a: Int = sliderAlign.value
  var s: Int = sliderSepa.value
  var c: Int = sliderCohe.value

  var r = 0

  //Size of the canvas
  val width = 1400
  val width2 = 400
  val height = 800
  val fullHeight = 810

  var initialBoids: Buffer[Boid] = Buffer[Boid]()

  /**
   * For loop to create 200 boids with randomized position and velocity. Initial acceleration is 0
   */
 for(i <- 0 to 200) {
    initialBoids += Boid(Vector2D(Random.nextDouble()*3, Random.nextDouble()*3), Vector2D(Simulation.width-(Random.nextDouble()*1000), (Random.nextDouble()*Simulation.height)), Vector2D(0, 0))
  }

  /**
   * Write initial state to file
   */
  FileManager.writeFile("start", initialBoids)

  /**
   *  Read initial state from file
   */
  var B: Boids.type = FileManager.readFile("start")

  def top = new MainFrame {

    /**
     * MainFrame includes a number of variables. Values of those variables influence on what kind
     * of window will be created.
     */

    title     = "Boids"
    resizable = true
    val button = new Button("Start")

    /**
     * The component declares here the minimum, maximum and preferred sizes
     */
    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)

    val arena = new BoxPanel(Orientation.Vertical) {

       override def paintComponent(g: Graphics2D) {
         g.setColor(Color.BLACK)
         g.fillRect(0, 0, width, fullHeight)
         if(r != 0) g.setColor(Color.white)
         if(r != 0) B.draw(g)
         g.setColor(Color.WHITE)
         g.fillRect(0, 0, width2, fullHeight)
         r = 1
      }
    }

    arena.contents += sliderAlign
    arena.contents += sliderCohe
    arena.contents += sliderSepa
    arena.contents += button
    contents = arena

    this.listenTo(sliderAlign)
    this.listenTo(sliderCohe)
    this.listenTo(sliderSepa)
    this.listenTo(button)

    this.reactions += {

      /**
       * Change values of the variables a, b, s when slider is being moved
       */
      case slideEvent: ValueChanged => {
        a = sliderAlign.value
        c = sliderCohe.value
        s = sliderSepa.value
      }

      case clickEvent: ButtonClicked => {
        val clickedButton = clickEvent.source
        val textOnButton = clickedButton.text
        if (textOnButton == "Start"){
          timer.start()  //Start simulation when "Start" button is pressed
        }
      }
    }

    val listener = new ActionListener(){
      def actionPerformed(e : java.awt.event.ActionEvent) = {
        B.step()
        arena.repaint()
      }
    }

    val timer = new javax.swing.Timer(10, listener)

  }
}

