package com.github.dongjinleekr.spark.example

import com.github.dongjinleekr.spark.dataset.Iris
import com.github.dongjinleekr.spark.dataset.Iris._
import org.apache.spark.ml.feature.{PCA, VectorAssembler}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

/**
  * PCA example, using iris dataset.
  **/
object Pca {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("PCA Example")
      .getOrCreate()

    // Read the file
    val raw = spark.read
      .schema(Iris.schema)
      .option("header", true)
      .csv("hdfs:///datasets/iris/data.csv")

    // Normalize:
    // 1. Combine the features into vector.
    // 2. Convert enumerating value into Int type.
    val assembler = new VectorAssembler()
      .setInputCols(Iris.schema.fields.map(_.name).slice(1, 5))
      .setOutputCol("features")

    def speciesToInt: (String => Int) = { s: String => Species.toInt(s) }

    val newSpecies = udf(speciesToInt).apply(col("species"))
    val df = assembler.transform(raw)
      .withColumn("species", newSpecies)
      .select("id", "features", "species")

    // PCA (2)
    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcaFeatures")
      .setK(2)
      .fit(df)

    val result = pca.transform(df).select("pcaFeatures")
    result.show(false)
  }
}
