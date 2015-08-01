/**
hanxiruideMacBook-Pro::hadoop-2.6.0 hanxirui $hadoop fs -copyFromLocal ../input/* /input

hanxiruideMacBook-Pro::hadoop-2.6.0 hanxirui $hadoop jar ../share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.0.jar wordcount /input /output

hanxiruideMacBook-Pro::hadoop-2.6.0 hanxirui $hadoop fs -cat /output/*

hadoop jar bigdatademo.jar com.hxr.bigdata.hadoop.firststep.WordCount /input /output

 * <br>
 *  
 * <p>
 * Create on : 2015年7月30日<br>
 * <p>
 * </p>
 * <br>
 * @author hanxirui<br>
 * @version bigdatademo v1.0
 * <p>
 *<br>
 * <strong>Modify History:</strong><br>
 * user     modify_date    modify_content<br>
 * -------------------------------------------<br>
 * <br>
 */
package com.hxr.bigdata.hadoop.firststep;