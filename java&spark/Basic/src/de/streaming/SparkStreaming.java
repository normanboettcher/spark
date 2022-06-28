package de.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.streaming.Duration;

import org.apache.spark.streaming.StreamingContext;

import de.basic.util.SparkBuilder;

public class SparkStreaming {
	public static void main(String[] args) {
		SparkBuilder sBuilder = new SparkBuilder();
		SparkConf conf = sBuilder.buildSparkConf();
		SparkContext context = sBuilder.getContext();
		
		StreamingContext sContext = new StreamingContext(conf, new Duration(500));
	}
	
}
