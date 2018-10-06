package com.hxr.bigdata.hadoop.definitive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *  * export HADOOP_CLASSPATH=bigdatademo.jar
 *  * bin/hadoop com.hxr.bigdata.hadoop.definitive.MaxTemperature input/ncdc/sample.txt output
 * The type Max temperature.
 */
public class MaxTemperature {
    public static void main(String[] args) throws Exception {

//        Job job = new Job();
        Configuration conf = new Configuration();
        // new Job();
        Job job = Job.getInstance(conf, "MaxTemperature");
        job.setJarByClass(MaxTemperature.class);
        job.setJobName("Max temperature");

        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);
        job.setCombinerClass(MaxTemperatureReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true)?0:1);

    }
}
