package bedex.biz.prev

import org.scalatest.FunSuite
import org.prevayler.Transaction
import org.prevayler.TransactionWithQuery

import org.prevayler.PrevaylerFactory

class PrevaylerSuite extends FunSuite {

  test("Can prevayler prevail ?") {
    val factory = PrevaylerFactory.createPrevayler[FruitBasket](new FruitBasket)
    factory.execute(new CreateFruit("mellon"))
    factory.execute(new CreateFruit("water mellon"))
    val fs = factory.execute(new GetFruits("mellon"))
    assert(fs.size === 2)
  }

}
