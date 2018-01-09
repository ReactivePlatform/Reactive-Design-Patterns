package chapter03.future

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import java.util.concurrent.ForkJoinPool

@RunWith(classOf[JUnit4])
class StagedFuturesForExampleTest {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(new ForkJoinPool())
  implicit val timeout: FiniteDuration = 250 milliseconds

  @Test
  def testInventoryCount: Unit = {
    val stagedFutures = new StagedFuturesForExample(new InventoryService() {
      def currentInventoryInWarehouse(productSku: Long, postalCode: String): Long = {
        5
      }
      def currentInventoryOverallByWarehouse(productSku: Long): Map[String, Long] = {
        Map("212" -> 407L, "312" -> 81L, "412" -> 6L)
      }
    })

    val results = stagedFutures.getProductInventoryByPostalCode(1234L, "212")
    results.foreach {
      case (local, overall) =>
        org.junit.Assert.assertEquals(local, 5)
        org.junit.Assert.assertEquals(overall("212"), 407L)
    }
  }
}