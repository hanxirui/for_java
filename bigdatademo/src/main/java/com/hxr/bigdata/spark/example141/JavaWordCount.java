/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hxr.bigdata.spark.example141;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public final class JavaWordCount {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(final String[] args) throws Exception {


    // 1.如果从文件生成RDD
    // if (args.length < 1) {
    // System.err.println("Usage: JavaWordCount <file>");
    // System.exit(1);
    // }
    // 对于所有的 Spark 程序而言，要进行任何操作，首先要创建一个 Spark 的上下文，
    // 在创建上下文的过程中，程序会向集群申请资源以及构建相应的运行环境。
    SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount");
    JavaSparkContext ctx = new JavaSparkContext(sparkConf);

    // 利用 textFile 接口从文件系统中读入指定的文件，返回一个 RDD 实例对象。
    // RDD 的初始创建都是由 SparkContext 来负责的，将内存中的集合或者外部文件系统作为输入源

    // 1.从文件生成RDD
    // JavaRDD<String> lines = ctx.textFile(args[0], 1);

    // 2.从HDFS生成RDD hdfs://127.0.0.1:9000
    JavaRDD<String> lines = ctx.textFile("/input/hdfs.txt", 1);

    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      
      public Iterable<String> call(final String s) {
        return Arrays.asList(SPACE.split(s));
      }
    });

    JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
      
      public Tuple2<String, Integer> call(final String s) {
        return new Tuple2<String, Integer>(s, 1);
      }
    });

    JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
      
      public Integer call(final Integer i1, final Integer i2) {
        return i1 + i2;
      }
    });

    List<Tuple2<String, Integer>> output = counts.collect();
    for (Tuple2<?,?> tuple : output) {
      System.out.println(tuple._1() + ": " + tuple._2());
    }
    ctx.stop();
  }
}
