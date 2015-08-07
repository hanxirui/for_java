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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class JavaSparkSQL {
    public static class Person implements Serializable {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(final int age) {
            this.age = age;
        }
    }

    public static void main(final String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf().setAppName("JavaSparkSQL");
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        SQLContext sqlContext = new SQLContext(ctx);

        System.out.println("=== Data source: RDD ===");
        // Load a text file and convert each line to a Java Bean.
        // 如果预先知道对应的javabean可以使用这中方法
//        实际读的是 hdfs://127.0.0.1:9000/spark/people.txt
        JavaRDD<Person> people = ctx.textFile("/spark/people.txt").map(
                new Function<String, Person>() {

                    public Person call(final String line) {
                        String[] parts = line.split(",");

                        Person person = new Person();
                        person.setName(parts[0]);
                        person.setAge(Integer.parseInt(parts[1].trim()));

                        return person;
                    }
                });

        // Apply a schema to an RDD of Java Beans and register it as a table.
        // 将schema（即javabean）应用到RDD上，并注册为一个表
        DataFrame schemaPeople = sqlContext.createDataFrame(people, Person.class);
        schemaPeople.registerTempTable("people");

        // SQL can be run over RDDs that have been registered as tables.
        // 这样就可以在上面执行sql了
        DataFrame teenagers = sqlContext.sql("SELECT name FROM people WHERE age >= 13 AND age <= 19");

        // The results of SQL queries are DataFrames and support all the normal RDD operations.
        // The columns of a row in the result can be accessed by ordinal.
//        查询结果是一个DataFrame，可以转为RDD并支持一般的RDD操作
        List<String> teenagerNames = teenagers.toJavaRDD().map(new Function<Row, String>() {

            public String call(final Row row) {
                return "Name: " + row.getString(0);
            }
        }).collect();
        for (String name : teenagerNames) {
            System.out.println(name);
        }
//        ------------------------预先不知道javabean--------------------------
     // The schema is encoded in a string
        String schemaString = "name age";

        // Generate the schema based on the string of schema
        List<StructField> fields = new ArrayList<StructField>();
        for (String fieldName: schemaString.split(" ")) {
          fields.add(DataTypes.createStructField(fieldName, DataTypes.StringType, true));
        }
        StructType schema = DataTypes.createStructType(fields);

     // Load a text file and convert each line to a JavaBean.
        JavaRDD<String> peopleT = ctx.textFile("/spark/people.txt");
        
        // Convert records of the RDD (people) to Rows.
        JavaRDD<Row> rowRDD = peopleT.map(
          new Function<String, Row>() {
            public Row call(final String record) throws Exception {
              String[] fields = record.split(",");
              return RowFactory.create(fields[0], fields[1].trim());
            }
          });

        // Apply the schema to the RDD.
        DataFrame peopleDataFrame = sqlContext.createDataFrame(rowRDD, schema);

        // Register the DataFrame as a table.
        peopleDataFrame.registerTempTable("people");

        // SQL can be run over RDDs that have been registered as tables.
        DataFrame results = sqlContext.sql("SELECT name FROM people");

        // The results of SQL queries are DataFrames and support all the normal RDD operations.
        // The columns of a row in the result can be accessed by ordinal.
        List<String> names = results.javaRDD().map(new Function<Row, String>() {
          public String call(final Row row) {
            return "Name: " + row.getString(0);
          }
        }).collect();

        System.out.println("=== Data source: Parquet File ===");
        // DataFrames can be saved as parquet files, maintaining the schema information.
//        默认都是对hdfs的操作，这个保存的路径为hdfs://127.0.0.1:9000/user/hanxirui/people.parquet
//        SaveMode.ErrorIfExists (default)  When saving a DataFrame to a data source, if data already exists, an exception is expected to be thrown.
//        SaveMode.Append When saving a DataFrame to a data source, if data/table already exists, contents of the DataFrame are expected to be appended to existing data.
//        SaveMode.Overwrite  Overwrite mode means that when saving a DataFrame to a data source, if data/table already exists, existing data is expected to be overwritten by the contents of the DataFrame.
//        SaveMode.Ignore  Ignore mode means that when saving a DataFrame to a data source, if data already exists, the save operation is expected to not save the contents of the DataFrame and to not change the existing data. This is similar to a CREATE TABLE IF NOT EXISTS in SQL.
        schemaPeople.write().mode(SaveMode.Ignore).parquet("people.parquet");

        // Read in the parquet file created above.
        // Parquet files are self-describing so the schema is preserved.
        // The result of loading a parquet file is also a DataFrame.
        DataFrame parquetFile = sqlContext.read().parquet("people.parquet");

        // Parquet files can also be registered as tables and then used in SQL statements.
        parquetFile.registerTempTable("parquetFile");
        DataFrame teenagers2 = sqlContext.sql("SELECT name FROM parquetFile WHERE age >= 13 AND age <= 19");
        teenagerNames = teenagers2.toJavaRDD().map(new Function<Row, String>() {

            public String call(final Row row) {
                return "Name: " + row.getString(0);
            }
        }).collect();
        for (String name : teenagerNames) {
            System.out.println(name);
        }

        System.out.println("=== Data source: JSON Dataset ===");
        // A JSON dataset is pointed by path.
        // The path can be either a single text file or a directory storing text files.
        String path = "/spark/people.json";
        // Create a DataFrame from the file(s) pointed by path
        DataFrame peopleFromJsonFile = sqlContext.read().json(path);

        // Because the schema of a JSON dataset is automatically inferred, to write queries,
        // it is better to take a look at what is the schema.
        peopleFromJsonFile.printSchema();
        // The schema of people is ...
        // root
        // |-- age: IntegerType
        // |-- name: StringType

        // Register this DataFrame as a table.
        peopleFromJsonFile.registerTempTable("people");

        // SQL statements can be run by using the sql methods provided by sqlContext.
        DataFrame teenagers3 = sqlContext.sql("SELECT name FROM people WHERE age >= 13 AND age <= 19");

        // The results of SQL queries are DataFrame and support all the normal RDD operations.
        // The columns of a row in the result can be accessed by ordinal.
        teenagerNames = teenagers3.toJavaRDD().map(new Function<Row, String>() {

            public String call(final Row row) {
                return "Name: " + row.getString(0);
            }
        }).collect();
        for (String name : teenagerNames) {
            System.out.println(name);
        }

        // Alternatively, a DataFrame can be created for a JSON dataset represented by
        // a RDD[String] storing one JSON object per string.
        List<String> jsonData = Arrays
                .asList("{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}");
        JavaRDD<String> anotherPeopleRDD = ctx.parallelize(jsonData);
        DataFrame peopleFromJsonRDD = sqlContext.read().json(anotherPeopleRDD.rdd());

        // Take a look at the schema of this new DataFrame.
        peopleFromJsonRDD.printSchema();
        // The schema of anotherPeople is ...
        // root
        // |-- address: StructType
        // | |-- city: StringType
        // | |-- state: StringType
        // |-- name: StringType

        peopleFromJsonRDD.registerTempTable("people2");

        DataFrame peopleWithCity = sqlContext.sql("SELECT name, address.city FROM people2");
        List<String> nameAndCity = peopleWithCity.toJavaRDD().map(new Function<Row, String>() {

            public String call(final Row row) {
                return "Name: " + row.getString(0) + ", City: " + row.getString(1);
            }
        }).collect();
        for (String name : nameAndCity) {
            System.out.println(name);
        }

        ctx.stop();
    }
}
