package de.basic.util;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;

import lombok.Getter;

@Getter
public class SparkBuilder {
	
	SparkConf conf;
	SparkSession session;
	SparkContext context;
			
	public SparkConf buildSparkConf() {
		conf = new SparkConf()
				.setAppName("basics").setMaster("local")
				.set("spark.hadoop.fs.default.name", "hdfs://master:9000")
				.set("spark.hadoop.fs.defaultFS", "hdfs://master:9000");
		return conf;
	}
	
	public SparkSession buildSession() {
		context =  new SparkContext(conf);
		session = new SparkSession(context);
		return session;
	}
	
}
