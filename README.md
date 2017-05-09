# spark-dataset

Convenience loader methods for common datasets, which can be used for testing in both of Spark
Application and REPL environment.

You can launch a spark shell with builded jar files with the command following:

```sh
spark-shell --driver-class-path $(echo target/*/*.jar | tr ' ' ',')
```

Following example shows how you can read and manipulate the supported dataset, in this case, the titanic dataset.

```sh
import com.github.dongjinleekr.spark.dataset.Titanic._

val spark = SparkSession
  .builder()
  .appName("Spark Dataset Example")
  .getOrCreate()

import spark.implicits._
import Titanic.implicits._

// Read dataset as DataFrame.
val df = spark.read
  .schema(Titanic.schema)
  .option("header", true)
  .csv("hdfs:///datasets/titanic/data.csv")
df.show(10)

// Convert DataFrame to DataSet.
val ds = df.as[Passenger]
ds.show(10)
ds.printSchema()
```

## Supported Datasets

- [Iris](https://www.kaggle.com/uciml/iris)
- [Titanic Survivors](https://www.kaggle.com/c/titanic)

