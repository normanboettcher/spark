package de.basic.util;

import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class FileReaderFromHDFS {
	
	public static Dataset<Row> readCSVFromHDFSToSpark(SparkSession s,
			SparkContext c, String path ) {
		Dataset<Row> data = s.read()
		.option("header", true)
		.csv(
				c.getConf().get("spark.hadoop.fs.defaultFS") 
				+ path)
		.cache();
		return data;
	}
}
