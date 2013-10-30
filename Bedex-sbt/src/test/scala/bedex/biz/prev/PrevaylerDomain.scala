package bedex.biz.prev

import java.util.Date
import scala.collection.mutable._
import org.prevayler.TransactionWithQuery

class PrevaylerDomain {

}


class FruitBasket extends Serializable {

  val root: Buffer[Fruit] = new ListBuffer[Fruit]()

}

class Fruit(val name: String, var amount: Int) extends Serializable

//class FruitInc extends Transaction[Fruit] {
//  
//  override def executeOn(what: Fruit, when: java.util.Date) {
//    what.amount += 1
//  }
//  
//}

class CreateFruit(val name: String) extends TransactionWithQuery[FruitBasket, Fruit] {

  override def executeAndQuery(basket: FruitBasket, when: Date): Fruit = {
    val f = new Fruit(name, 0)
    basket.root += f
    f
  }

}

class GetFruits(val name: String) extends TransactionWithQuery[FruitBasket, List[Fruit]] {

  override def executeAndQuery(basket: FruitBasket, when: Date): List[Fruit] = {
    val r = basket.root.filter(_.name.contains(name))
    r.toList
  }

}