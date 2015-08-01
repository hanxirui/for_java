package com.hxr.bigdata.spark.chapter1;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

public class Introduce {
    public int wordCount() {
        
        SparkConf conf = new SparkConf().setAppName("WordCount").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Spark revolves around the concept of a resilient distributed dataset (RDD), which is a fault-tolerant
        // collection of elements that can be operated on in parallel. There are two ways to create RDDs: parallelizing
        // an existing collection in your driver program, or referencing a dataset in an external storage system, such
        // as a shared filesystem, HDFS, HBase, or any data source offering a Hadoop InputFormat.

        // 1.Parallelized collections are created by calling JavaSparkContext’s parallelize method on an existing
        // Collection in your driver program. The elements of the collection are copied to form a distributed dataset
        // that can be operated on in parallel. For example, here is how to create a parallelized collection holding the
        // numbers 1 to 5:
        // List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        // JavaRDD<Integer> distData = sc.parallelize(data);
        // distData.reduce((a, b) -> a + b);

        // 2.Spark can create distributed datasets from any storage source supported by Hadoop, including your local
        // file system, HDFS, Cassandra, HBase, Amazon S3, etc. Spark supports text files, SequenceFiles, and any other
        // Hadoop InputFormat.
        // Text file RDDs can be created using SparkContext’s textFile method. This method takes an URI for the file
        // (either a local path on the machine, or a hdfs://, s3n://, etc URI) and reads it as a collection of lines.
        // Here is an example invocation:
        // JavaRDD<String> distFile = sc.textFile("data.txt");
        // distFile.map(s -> s.length()).reduce((a, b) -> a + b).

        // Apart from text files, Spark’s Java API also supports several other data formats:
        //
        // JavaSparkContext.wholeTextFiles lets you read a directory containing multiple small text files, and returns
        // each of them as (filename, content) pairs. This is in contrast with textFile, which would return one record
        // per line in each file.
        //
        // For SequenceFiles, use SparkContext’s sequenceFile[K, V] method where K and V are the types of key and values
        // in the file. These should be subclasses of Hadoop’s Writable interface, like IntWritable and Text.
        //
        // For other Hadoop InputFormats, you can use the JavaSparkContext.hadoopRDD method, which takes an arbitrary
        // JobConf and input format class, key class and value class. Set these the same way you would for a Hadoop job
        // with your input source. You can also use JavaSparkContext.newAPIHadoopRDD for InputFormats based on the “new”
        // MapReduce API (org.apache.hadoop.mapreduce).
        //
        // JavaRDD.saveAsObjectFile and JavaSparkContext.objectFile support saving an RDD in a simple format consisting
        // of serialized Java objects. While this is not as efficient as specialized formats like Avro, it offers an
        // easy way to save any RDD.

        // JavaRDD<String> lines = sc.textFile("data.txt");
        // JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
        // int totalLength = lineLengths.reduce((a, b) -> a + b);
        // lineLengths.persist();

        JavaRDD<String> lines = sc.textFile("data.txt");
        JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
            public Integer call(final String s) {
                return s.length();
            }
        });
        int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
            public Integer call(final Integer a, final Integer b) {
                return a + b;
            }
        });

        // class GetLength implements Function<String, Integer> {
        // public Integer call(String s) { return s.length(); }
        // }
        // class Sum implements Function2<Integer, Integer, Integer> {
        // public Integer call(Integer a, Integer b) { return a + b; }
        // }
        //
        // JavaRDD<String> lines = sc.textFile("data.txt");
        // JavaRDD<Integer> lineLengths = lines.map(new GetLength());
        // int totalLength = lineLengths.reduce(new Sum());

//        JavaRDD<String> lines = sc.textFile("data.txt");
//        JavaPairRDD<String, Integer> pairs = lines.mapToPair(s -> new Tuple2(s, 1));
//        JavaPairRDD<String, Integer> counts = pairs.reduceByKey((a, b) -> a + b);
        
        
        
//        Bundling Your Application’s Dependencies
//        If your code depends on other projects, you will need to package them alongside your application in order to distribute the code to a Spark cluster. To do this, create an assembly jar (or “uber” jar) containing your code and its dependencies. Both sbt and Maven have assembly plugins. When creating assembly jars, list Spark and Hadoop as provided dependencies; these need not be bundled since they are provided by the cluster manager at runtime. Once you have an assembled jar you can call the bin/spark-submit script as shown here while passing your jar.
//
//        For Python, you can use the --py-files argument of spark-submit to add .py, .zip or .egg files to be distributed with your application. If you depend on multiple Python files we recommend packaging them into a .zip or .egg.
//
//        Launching Applications with spark-submit
//        Once a user application is bundled, it can be launched using the bin/spark-submit script. This script takes care of setting up the classpath with Spark and its dependencies, and can support different cluster managers and deploy modes that Spark supports:
//
//        ./bin/spark-submit \
//        --class <main-class>
//        --master <master-url> \
//        --deploy-mode <deploy-mode> \
//        --conf <key>=<value> \
//        ... # other options
//        <application-jar> \
//        [application-arguments]
//      Some of the commonly used options are:
//
//      --class: The entry point for your application (e.g. org.apache.spark.examples.SparkPi)
//      --master: The master URL for the cluster (e.g. spark://23.195.26.187:7077)
//      --deploy-mode: Whether to deploy your driver on the worker nodes (cluster) or locally as an external client (client) (default: client) †
//      --conf: Arbitrary Spark configuration property in key=value format. For values that contain spaces wrap “key=value” in quotes (as shown).
//      application-jar: Path to a bundled jar including your application and all dependencies. The URL must be globally visible inside of your cluster, for instance, an hdfs:// path or a file:// path that is present on all nodes.
//      application-arguments: Arguments passed to the main method of your main class, if any
//      † A common deployment strategy is to submit your application from a gateway machine that is physically co-located with your worker machines (e.g. Master node in a standalone EC2 cluster). In this setup, client mode is appropriate. In client mode, the driver is launched directly within the spark-submit process which acts as a client to the cluster. The input and output of the application is attached to the console. Thus, this mode is especially suitable for applications that involve the REPL (e.g. Spark shell).
//
//      Alternatively, if your application is submitted from a machine far from the worker machines (e.g. locally on your laptop), it is common to use cluster mode to minimize network latency between the drivers and the executors. Note that cluster mode is currently not supported for Mesos clusters. Currently only YARN supports cluster mode for Python applications.
//
//      For Python applications, simply pass a .py file in the place of <application-jar> instead of a JAR, and add Python .zip, .egg or .py files to the search path with --py-files.
//
//      There are a few options available that are specific to the cluster manager that is being used. For example, with a Spark Standalone cluster with cluster deploy mode, you can also specify --supervise to make sure that the driver is automatically restarted if it fails with non-zero exit code. To enumerate all such options available to spark-submit, run it with --help. Here are a few examples of common options:
//
//      # Run application locally on 8 cores
//      ./bin/spark-submit \
//        --class org.apache.spark.examples.SparkPi \
//        --master local[8] \
//        /path/to/examples.jar \
//        100
//
//      # Run on a Spark Standalone cluster in client deploy mode
//      ./bin/spark-submit \
//        --class org.apache.spark.examples.SparkPi \
//        --master spark://207.184.161.138:7077 \
//        --executor-memory 20G \
//        --total-executor-cores 100 \
//        /path/to/examples.jar \
//        1000
//
//      # Run on a Spark Standalone cluster in cluster deploy mode with supervise
//      ./bin/spark-submit \
//        --class org.apache.spark.examples.SparkPi \
//        --master spark://207.184.161.138:7077 \
//        --deploy-mode cluster
//        --supervise
//        --executor-memory 20G \
//        --total-executor-cores 100 \
//        /path/to/examples.jar \
//        1000
//
//      # Run on a YARN cluster
//      export HADOOP_CONF_DIR=XXX
//      ./bin/spark-submit \
//        --class org.apache.spark.examples.SparkPi \
//        --master yarn-cluster \  # can also be `yarn-client` for client mode
//        --executor-memory 20G \
//        --num-executors 50 \
//        /path/to/examples.jar \
//        1000
//
//      # Run a Python application on a Spark Standalone cluster
//      ./bin/spark-submit \
//        --master spark://207.184.161.138:7077 \
//        examples/src/main/python/pi.py \
//        1000
        return 0;
    }
}
