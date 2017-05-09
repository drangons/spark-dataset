package com.github.dongjinleekr.spark.dataset

import org.apache.spark.sql.types._

/**
  * Helper classes for [[https://www.kaggle.com/uciml/iris the Iris dataset]] from
  * [[https://www.kaggle.com/ Kaggle]].
  */
object Iris {

  // Schema for DataFrame
  val schema = StructType(
    Seq(
      StructField("id", IntegerType, false),
      StructField("sepalLength", DoubleType, false),
      StructField("sepalWidth", DoubleType, false),
      StructField("petalLength", DoubleType, false),
      StructField("petalWidth", DoubleType, false),
      StructField("species", StringType, false)
    )
  )

  // Enum types
  sealed trait Species {
    def value: Int
    def name: String
  }

  object Species {
    def toInt(str: String): Int = {
      str match {
        case Setosa.name => Setosa.value
        case Versicolor.name => Versicolor.value
        case Virginica.name => Virginica.value
        case _ => throw new IllegalArgumentException
      }
    }

    def apply(int: Int): Species = {
      int match {
        case Setosa.value => Setosa
        case Versicolor.value => Versicolor
        case Virginica.value => Virginica
        case _ => throw new IllegalArgumentException
      }
    }

    def unapply(species: Species): Int = species.value

  }

  final case object Setosa extends Species {
    val value = 1
    val name = "Iris-setosa"
  }

  final case object Versicolor extends Species {
    val value = 2
    val name = "Iris-versicolor"
  }

  final case object Virginica extends Species {
    val value = 3
    val name = "Iris-virginica"
  }

  object implicits {
    implicit def intToSpecies(int: Int): Species = Species.apply(int)
  }

  case class Plant(id: Int,
                   sepalLength: Double,
                   sepalWidth: Double,
                   petalLength: Double,
                   petalWidth: Double,
                   species: Int)

}
