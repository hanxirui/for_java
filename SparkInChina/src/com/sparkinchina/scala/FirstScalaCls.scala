package com.sparkinchina.scala

/**
 * @author hanxirui
 */
class FirstScalaCls {
  
}

object FirstScalCls{
  def looper(x:Long,y:Long):Long={
    var a = x
    var b = y
    while(a!=0){
      val temp = a
      a = b % a
      b = temp
    }
    b
  }
  
//  var line = ""
//  
//  do{
//    line = readLine()
//    println("read:" + line)
//    
//    
//  }while(line!="")
//    
  
  def main(args: Array[String]): Unit = {
//    println("Hello Scala!");
//    var file = "Scala.txt"
//    if(!args.isEmpty) file = args(0)
//    
//    println(file)
    
    println(looper(100,290));
    
    for(i <- 1 to 10){
      println(i);
    }
    
    val files = (new java.io.File(".")).listFiles()
    for(file <- files){
      println(file)
    }
    
  }
}
































