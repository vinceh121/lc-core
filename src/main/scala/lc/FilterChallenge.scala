package lc

import com.sksamuel.scrimage._
import com.sksamuel.scrimage.filter._
import java.awt.image.BufferedImage
import java.awt.Font
import java.awt.Color

class FilterChallenge extends ChallengeProvider {
  def getId = "filter"
  def returnChallenge(): Challenge = {
    val filterTypes = List(new FilterType1, new FilterType2)
    val r = new scala.util.Random
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    val n = 8
    val secret = Stream.continually(r.nextInt(alphabet.size)).map(alphabet).take(n).mkString
    val canvas = new BufferedImage(225, 50, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)
    g.setColor(Color.BLACK)
    g.setFont(new Font("Serif", Font.PLAIN, 30))
    g.drawString(secret, 5, 30)
    g.dispose()
    var image = new Image(canvas, ImageMetadata.empty)
    val s = scala.util.Random.nextInt(2)
    image = filterTypes(s).applyFilter(image)
    new Challenge(image.bytes, "image/png", secret)
  }
  def checkAnswer(secret: String, answer: String): Boolean = {
    secret == answer
  }
}

trait FilterType {
  def applyFilter(image: Image): Image
}

class FilterType1 extends FilterType {
  override def applyFilter(image: Image): Image = {
    val blur = new GaussianBlurFilter(2)
    val smear = new SmearFilter(com.sksamuel.scrimage.filter.SmearType.Circles, 10, 10, 10, 0, 1)
    val diffuse = new DiffuseFilter(2)
    blur.apply(image)
    diffuse.apply(image)
    smear.apply(image)
    image
  }
}

class FilterType2 extends FilterType {
  override def applyFilter(image: Image): Image = {
    val smear = new SmearFilter(com.sksamuel.scrimage.filter.SmearType.Circles, 10, 10, 10, 0, 1)
    val diffuse = new DiffuseFilter(1)
    val ripple = new RippleFilter(com.sksamuel.scrimage.filter.RippleType.Noise, 1, 1, 0.005.toFloat, 0.005.toFloat)
    diffuse.apply(image)
    ripple.apply(image)
    smear.apply(image)
    image
  }
}

