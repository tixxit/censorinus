import org.scalatest._
import scala.collection.mutable.ArrayBuffer
import github.gphat.censorinus.{Client,Metric,MetricSender}
import github.gphat.censorinus.statsd.Encoder

class TestSender extends MetricSender {
  val buffer = new ArrayBuffer[String]()

  def clear = buffer.clear

  def send(message: String): Unit = {
    buffer.append(message)
  }

  def shutdown: Unit = ()

  def getBuffer = buffer
}

class ClientSpec extends FlatSpec with Matchers {

  "ClientSpec" should "deal with gauges" in {
    val client = new Client(encoder = Encoder, sender = new TestSender())
    client.enqueue(Metric(name = "foobar", value = "1.0", metricType = "g"))
    val m = client.getQueue.poll
    m.name should be ("foobar")
    m.value should be ("1.0")
    m.metricType should be ("g")
    client.shutdown
  }
}
