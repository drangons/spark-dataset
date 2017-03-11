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
      StructField("age", FloatType, true),
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
  sealed trait PClass {
    def value: Int
  }
  object PClass {
    def apply(int: Int): PClass = {
      int match {
        case FirstClass.value => FirstClass
        case SecondClass.value => SecondClass
        case ThirdClass.value => ThirdClass
        case _ => throw new IllegalArgumentException
      }
    }

    def unapply(pClass: PClass): Int = pClass.value
  }
  final case object FirstClass extends PClass {
    val value = 1
  }
  final case object SecondClass extends PClass {
    val value = 2
  }
  final case object ThirdClass extends PClass {
    val value = 3
  }

  // Sex: Male or Female
  sealed trait Sex {
    def value: String
  }
  object Sex {
    def apply(str: String) = {
      str match {
        case Male.value => Male
        case Female.value => Female
        case _ => throw new IllegalArgumentException
      }
    }

    def unapply(sex: Sex): String = sex.value
  }
  final case object Male extends Sex {
    val value = "male"
  }
  final case object Female extends Sex {
    val value = "female"
  }

  // Port of Embarkation
  sealed trait Embarkation {
    def value: String
  }
  object Embarkation {
    def apply(str: String) = {
      str match {
        case Cherbourg.value => Cherbourg
        case Queenstown.value => Queenstown
        case Southampton.value => Southampton
        case _ => Unknown
      }
    }

    def unapply(embarkation: Embarkation): String = embarkation.value
  }
  final case object Cherbourg extends Embarkation {
    val value = "c"
  }
  final case object Queenstown extends Embarkation {
    val value = "q"
  }
  final case object Southampton extends Embarkation {
    val value = "s"
  }
  final case object Unknown extends Embarkation {
    val value = ""
  }

  object implicits {
    implicit val optionalFloat: Encoder[Option[Float]] = ExpressionEncoder()
    implicit val optionalStringEncoder: Encoder[Option[String]] = ExpressionEncoder()
  }

  // Type for Business Logic: it provides some additional functionality and clarity.
  // The name and type of some of the members of this type is different from its schema:
  // isSurvived, age, cabin, embarked.
  case class Passenger(id: Int, survival: Boolean, _pClass: Int, name: String, _sex: String,
                       age: Option[Float], sibsp: Int, parch: Int, ticket: String, fare: Float,
                       cabin: Option[String], _embarked: String) {
    def pClass = PClass(_pClass)
    def sex = Sex(_sex)
    def embarkation = Embarkation(_embarked)
  }

}
