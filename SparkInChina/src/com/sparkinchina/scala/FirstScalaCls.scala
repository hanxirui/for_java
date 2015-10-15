package com.sparkinchina.scala

/**
 * @author hanxirui
 */
class FirstScalaCls {
  
}

object FirstScalCls{
  def main(args: Array[String]): Unit = {
    println("Hello Scala!");
    var file = "Scala.txt"
    if(!args.isEmpty) file = args(0)
    
    println(file)
  }
}