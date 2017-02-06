package com.github.dongjinleekr.spark.dataset

import java.util.Locale

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import scala.reflect.ClassTag

/**
  * Helper classes for [[https://www.kaggle.com/c/titanic the Titanic dataset]] from
  * [[https://www.kaggle.com/ Kaggle]].
  */
object Titanic {

  val schema = StructType(
    Seq(
      StructField("id", IntegerType, false),
      StructField("survival", IntegerType, false),
      StructField("pclass", IntegerType, false),
      StructField("name", StringType, false),
      StructField("sex", StringType, false),
      StructField("age", StringType, true),
      StructField("sibsp", IntegerType, false),
      StructField("parch", IntegerType, false),
      StructField("ticket", StringType, false),
      StructField("fare", FloatType, false),
      StructField("cabin", StringType, true),
      StructField("embarked", StringType, true)
    )
  )

  // Passenger Class: 1st, 2nd or 3rd class
  sealed trait PClass

  final case object FirstClass extends PClass

  final case object SecondClass extends PClass

  final case object ThirdClass extends PClass

  // Sex: Male or Female
  sealed trait Sex

  final case object Male extends Sex

  final case object Female extends Sex

  // Port of Embarkation
  sealed trait Embarkation

  final case object Cherbourg extends Embarkation

  final case object Queenstown extends Embarkation

  final case object Southampton extends Embarkation

  final case object Unknown extends Embarkation

  case class Passenger(id: Int, survived: Boolean, pClass: PClass, name: String, sex: Sex,
                       age: Option[Int], sibsp: Int, parch: Int, ticket: String, fare: Float,
                       cabin: Option[String], embarkation: Embarkation)

  // Implicit Kryo encoder
  // see: http://stackoverflow.com/questions/36144618/spark-kryo-register-a-custom-serializer
  implicit def kryoEncoder[A](implicit ct: ClassTag[A]) = org.apache.spark.sql.Encoders.kryo[A](ct)

  def toPassenger(row: Row): Passenger = {
    val id: Int = row.getInt(0)
    val survived: Boolean = row.getInt(1) match {
      case 0 => false
      case 1 => true
    }
    val pClass: PClass = row.getInt(2) match {
      case 1 => FirstClass
      case 2 => SecondClass
      case 3 => ThirdClass
    }
    val name: String = row.getString(3)
    val sex: Sex = row.getString(4) match {
      case "male" => Male
      case "female" => Female
    }
    val age: Option[Int] = row.getAs[String](5) match {
      case null => None
      case str => {
        val pointIndex: Int = str.indexOf('.')
        pointIndex match {
          case 0 => Some(Integer.valueOf(str))
          case 1 => Some(Integer.valueOf(str.substring(0, pointIndex)))
          case _ => None
        }
      }
    }
    val sibsp: Int = row.getInt(6)
    val parch: Int = row.getInt(7)
    val ticket: String = row.getString(8)
    val fare: Float = row.getFloat(9)
    val cabin: Option[String] = row.getString(10) match {
      case null => None
      case str => Some(str)
    }
    val embarkation: Embarkation = row.getString(11) match {
      case null => Unknown
      case s => s.toLowerCase(Locale.ROOT) match {
        case "c" => Cherbourg
        case "q" => Queenstown
        case "s" => Southampton
      }
    }

    Passenger(id, survived, pClass, name, sex, age, sibsp, parch, ticket, fare, cabin, embarkation)
  }
}
