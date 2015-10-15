/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.hxr.bigdata.spark.streaming;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.api.java.JavaStreamingContextFactory;

import scala.Tuple2;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * Counts words in text encoded with UTF8 received from the network every second. 
 * Usage: JavaRecoverableNetworkWordCount <hostname> <port> <checkpoint-directory> <output-file> 
 * <hostname> and <port> describe the TCP server that Spark
 * Streaming would connect to receive data. 
 * <checkpoint-directory> directory to HDFS-compatible file system which checkpoint data 
 * <output-file> file to which the word counts will be appended <checkpoint-directory> and <output-file>
 * must be absolute paths To run this on your local machine, you need to first run a Netcat server `$ nc -lk 9999` and
 * run the example as `$ ./bin/run-example org.apache.spark.examples.streaming.JavaRecoverableNetworkWordCount \
 * localhost 9999 ~/checkpoint/ ~/out` 
 ./bin/spark-submit --class com.hxr.bigdata.spark.streaming.NetworkWordCount --driver-class-path lib/guava-11.0.2.jar --master local[2] bigdatademo.jar localhost 9999 file:///Users/hanxirui/Documents/workspace/library/spark-1.4.1-bin-hadoop2.6/checkpoint/ out.txt 
 If the directory
 * ~/checkpoint/ does not exist (e.g. running for the first time), it will create a new StreamingContext (will print
 * "Creating new context" to the console). Otherwise, if checkpoint data exists in ~/checkpoint/, then it will create
 * StreamingContext from the checkpoint data. Refer to the online documentation for more details.
 * 
 * Spark执行一次后，会生成缓存，下次执行的时候不会执行main方法了。所以要执行 sudo rm -rf /private/var/folders/ 删除缓存
 */
public final class NetworkWordCount {
    private static final Pattern SPACE = Pattern.compile(" ");

    private static JavaStreamingContext createContext(final String ip, final int port,
        final String checkpointDirectory, final String outputPath) {

        // If you do not see this printed, that means the StreamingContext has been loaded
        // from the new checkpoint
        System.out.println("Creating new context");
        final File outputFile = new File(outputPath);
        
        SparkConf sparkConf = new SparkConf().setAppName("NetworkWordCount");
        // Create the context with a 1 second batch size
        // 首先通过 JavaStreamingContextFactory 创建 Spark Streaming 过程。
        // 构建一个 Spark Streaming 应用程序一般来说需要 4 个步骤。
        // 1构建 Streaming Context 对象
        // 与 Spark 初始需要创建 SparkContext 对象一样，使用 Spark Streaming 就需要创建 StreamingContext 对象。创建 StreamingContext 对象所需的参数与
        // SparkContext 基本一致，包括指明 master、设定名称等。需要注意的是参数 Second(1)，Spark Streaming 需要制定处理数据的时间间隔，如 1s，那么 Spark Streaming
        // 会以 1s 为时间窗口进行数据处理。此参数需要根据用户的需求和集群的处理能力进行适当的设置，它的生命周期会伴随整个 StreamingContext
        // 的生命周期且无法重新设置。因此，用户需要从需求和集群处理能力出发，设置一个合理的时间间隔。
        JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));
        
        // 持久化到文件
        ssc.checkpoint(checkpointDirectory);

        // Create a socket stream on target ip:port and count the
        // words in input stream of \n delimited text (eg. generated by 'nc')
        // 2创建 InputDStream
        // 如同 Strom 的 Spout 一样，Spark Streaming 需要指明数据源。例如 socketTextStream，Spark Streaming 将以套接字连接作为数据源读取数据。当然，Spark
        // Streaming 支持多种不同的数据源，包括 kafkaStream、flumeStream、fileStream、networkStream 等。
        JavaReceiverInputDStream<String> lines = ssc.socketTextStream(ip, port);
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            public Iterable<String> call(final String x) {
                return Lists.newArrayList(SPACE.split(x));
            }
        });
        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(final String s) {
                return new Tuple2<String, Integer>(s, 1);
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(final Integer i1, final Integer i2) {
                return i1 + i2;
            }
        });

//        UpdateStateByKey 原语用于记录历史记录。若不用 UpdateStateByKey 来更新状态，那么每次数据进来后分析完成，结果输出后将不再保存。
        
        // map(func) 方法返回一个新 DStream，其中的每一个元素都是通过将原 DStream 的每个元素作用于函数 func 得到的。
        // flatMap(func) 方法与 map 相似，不同之处在于每一个元素通过函数 func 可以产生出 0 个或多个新元素。
        // reduceByKey(func,numTasks) 方法将 DStream[(K,V)] 中的值 V 按键 K 使用聚合函数 func 聚合。默认情况下，将采用 Spark 的默认任务并行的提交任务 (本地环境下是
        // 2，集群环境下是 8)，可以通过配置 numTasks 设置不同的任务数量。
        // foreachRDD(func) 方法是基本的输出操作，将 DStream 中的每个 RDD 作用于函数 func 上，如输出每个 RDD 内的元素、将 RDD 保存到外部文件中。
//mapToPair 
        wordCounts.foreachRDD(new Function2<JavaPairRDD<String, Integer>, Time, Void>() {
            public Void call(final JavaPairRDD<String, Integer> rdd, final Time time) throws IOException {
                String counts = "Counts at time " + time + " " + rdd.collect();
                if (!outputFile.exists()) {
                    try {
                        outputFile.createNewFile();
                       
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                Files.append(counts + "\n", outputFile, Charset.defaultCharset());
                return null;
            }
        });

        return ssc;
    }

    // DStream 上的原语与 RDD 的类似，分为 Transformations（转换）和 Output
    // Operations（输出）两种，此外转换操作中还有一些比较特殊的原语，如：updateStateByKey()、transform() 以及各种 Window 相关的原语。
    // UpdateStateByKey 原语用于记录历史记录，上文中 Word Count 示例中就用到了该特性。若不用 UpdateStateByKey
    // 来更新状态，那么每次数据进来后分析完成，结果输出后将不再保存。如，若将上文清单 2 中的第 15 行替换为：
    // JavaPairDStream<String, Integer> counts = pairs.reduceByKey((i1, i2) -> (i1 + i2));
    // 那么输入：hellow world，结果则为：(hello,1)(world,1)，然后输入 hello spark，结果则为 (hello,1)(spark,1)。也就是不会保留上一次数据处理的结果。
    // 使用 UpdateStateByKey 原语用于需要记录的 State，可以为任意类型，如上例中即为 Optional<Intege>类型。
    // Transform() 原语允许 DStream 上执行任意的 RDD-to-RDD 函数，通过该函数可以方便的扩展 Spark API。

    public static void main(final String[] args) {
        if (args.length != 4) {
            System.err.println("You arguments were " + Arrays.asList(args));
            System.err.println("Usage: JavaRecoverableNetworkWordCount <hostname> <port> <checkpoint-directory>\n"
                + "     <output-file>. <hostname> and <port> describe the TCP server that Spark\n"
                + "     Streaming would connect to receive data. <checkpoint-directory> directory to\n"
                + "     HDFS-compatible file system which checkpoint data <output-file> file to which\n"
                + "     the word counts will be appended\n" + "\n"
                + "In local mode, <master> should be 'local[n]' with n > 1\n"
                + "Both <checkpoint-directory> and <output-file> must be absolute paths");
            System.exit(1);
        }

        final String ip = args[0];
        final int port = Integer.parseInt(args[1]);
        final String checkpointDirectory = args[2];
        final String outputPath = args[3];

        JavaStreamingContextFactory factory = new JavaStreamingContextFactory() {
            public JavaStreamingContext create() {
                return createContext(ip, port, checkpointDirectory, outputPath);
            }
        };

        JavaStreamingContext ssc = JavaStreamingContext.getOrCreate(checkpointDirectory, factory);
        ssc.start();
        ssc.awaitTermination();
    }
}
