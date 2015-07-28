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
        
        return 0;
    }
}
