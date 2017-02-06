# spark-dataset

Convenience loader methods for common datasets, which can be used for testing in both of Spark
Application and REPL environment.

You can launch a spark shell with builded jar files with the command following:

```sh
spark-shell --driver-class-path $(echo target/*/*.jar | tr ' ' ',')
```

Following example shows how you can read the supported dataset, in this case, the titanic dataset.

```sh
import com.github.dongjinleekr.spark.dataset.Titanic

val df = spark.read.schema(Titanic.schema).option("header", true).csv("hdfs:///datasets/titanic/data.csv")
val seq = df.map(Titanic.toPassenger).collect
```

## Supported Datasets

- [Titanic Survivors](https://www.kaggle.com/c/titanic)
