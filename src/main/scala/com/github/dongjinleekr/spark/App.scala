package com.github.dongjinleekr.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object App {
	def main(args: Array[String]) {
		val conf = new SparkConf().setAppName("spark-quickstart")
		val sc: SparkContext = new SparkContext(conf)

		val input: String = "gs://gce-input/data.txt"
		val output: String = "gs://gce-output/output"

		val textFile = sc.textFile(input)
		val counts = textFile
			.map(word => (word, 1))
			.reduceByKey(_ + _)
		counts.saveAsTextFile(output)
		for (arg <- args) {
			println(arg)
		}
	}
}
