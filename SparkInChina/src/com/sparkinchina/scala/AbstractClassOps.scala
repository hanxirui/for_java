package com.sparkinchina.scala

/**
 * @author hanxirui
 */
class AbstractClassOps {
  
}

abstract class Teacher{
  def teach
  def test
  val name : String
  val age : Int
}

class TeacherForMaths extends Teacher{
  def teach():Unit = {
    println("Teaching Math")
  }
  
  def test{
    println("Testing Math")
  }
  
  val name = "Rocky"
  val age = 33
}


object AbstractClassOps{
  def main(args: Array[String]): Unit = {
     val t = new TeacherForMaths
     t.teach()
     println(t.name)
  }
}