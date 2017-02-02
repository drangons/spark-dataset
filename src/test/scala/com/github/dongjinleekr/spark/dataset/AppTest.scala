package com.github.dongjinleekr.spark.dataset

import org.scalatest.FunSuite
import com.holdenkarau.spark.testing.{RDDComparisons, SharedSparkContext}

/*
* see: http://blog.cloudera.com/blog/2015/09/making-apache-spark-testing-easy-with-spark-testing-base/
* see: https://github.com/holdenk/spark-testing-base/wiki/SharedSparkContext
* */
class AppTest extends FunSuite with SharedSparkContext {
	test("test initializing spark context") {
		val list = List(1, 2, 3, 4)
		val rdd = sc.parallelize(list)

		assert(rdd.count === list.length)
	}
}
