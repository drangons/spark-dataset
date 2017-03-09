package com.github.dongjinleekr.spark.dataset

import org.apache.spark.sql.Encoder
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types._

/**
  * Helper classes for [[https://www.kaggle.com/c/titanic the Titanic dataset]] from
  * [[https://www.kaggle.com/ Kaggle]].
  */
object Titanic {

  // Schema for DataFrame
  val schema = StructType(
    Seq(
      StructField("id", IntegerType, false),
      StructField("survival", IntegerType, false),
      StructField("_pClass", IntegerType, false),
      StructField("name", StringType, false),
      StructField("_sex", StringType, false),
      StructField("age", StringType, true),
      StructField("sibsp", IntegerType, false),
      StructField("parch", IntegerType, false),
      StructField("ticket", StringType, false),
      StructField("fare", FloatType, false),
      StructField("cabin", StringType, true),
      StructField("_embarked", StringType, true)
    )
  )

  // Enum types
  // Passenger Class: 1st, 2nd or 3rd class
  sealed trait PClass
  object PClass {
    def apply(int: Int): PClass = {
      int match {
        case 1 => FirstClass
        case 2 => SecondClass
        case 3 => ThirdClass
        case _ => throw new IllegalArgumentException
      }
    }

    def unapply(pClass: PClass): Int = {
      pClass match {
        case FirstClass => 1
        case SecondClass => 2
        case ThirdClass => 3
      }
    }
  }
  final case object FirstClass extends PClass
  final case object SecondClass extends PClass
  final case object ThirdClass extends PClass

  // Sex: Male or Female
  sealed trait Sex
  object Sex {
    def apply(str: String) = {
      str match {
        case "male" => Male
        case "female" => Female
        case _ => throw new IllegalArgumentException
      }
    }

    def unapply(sex: Sex): String = {
      sex match {
        case Male => "male"
        case Female => "female"
      }
    }
  }
  final case object Male extends Sex
  final case object Female extends Sex

  // Port of Embarkation
  sealed trait Embarkation
  object Embarkation {
    def apply(str: String) = {
      str match {
        case "c" => Cherbourg
        case "q" => Queenstown
        case "s" => Southampton
        case _ => Unknown
      }
    }

    def unapply(embarkation: Embarkation): String = {
      embarkation match {
        case Cherbourg => "c"
        case Queenstown => "q"
        case Southampton => "s"
        case Unknown => ""
      }
    }
  }
  final case object Cherbourg extends Embarkation
  final case object Queenstown extends Embarkation
  final case object Southampton extends Embarkation
  final case object Unknown extends Embarkation

  object implicits {
    implicit def boolToInt(o: Boolean) = o match {
      case true => 1
      case false => 0
    }

    implicit def intToBool(e: Int) = e match {
      case 0 => false
      case 1 => true
    }

    implicit val optionalString: Encoder[Option[String]] = ExpressionEncoder()
  }

  // Type for Business Logic: it provides some additional functionality and clarity.
  // The name and type of some of the members of this type is different from its schema:
  // isSurvived, age, cabin, embarked.
  case class Passenger(id: Int, survival: Boolean, _pClass: Int, name: String, _sex: String,
                       age: String, sibsp: Int, parch: Int, ticket: String, fare: Float,
                       cabin: Option[String], _embarked: String) {
    def pClass = PClass(_pClass)
    def sex = Sex(_sex)
    def embarkation = Embarkation(_embarked)
  }

}
