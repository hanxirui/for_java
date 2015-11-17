package com.sparkinchina.scala

/**
 * @author hanxirui
 */
class OopInScala(name:String) {
  
  var age:Int = 27
  private[this] val country:String = "China"
  
   def sayHello(){
     println("hello " + name)
   }
}

object OopInScala{
   def main(args: Array[String]): Unit = {
     val p = new OopInScala("zhangsan")
     p.sayHello()
     
  }
}