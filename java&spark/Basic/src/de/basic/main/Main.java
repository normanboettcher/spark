package de.basic.main;

import de.basic.util.FileReaderFromHDFS;
import de.basic.util.IFootballDataConstants;
import de.basic.util.SparkBuilder;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

import de.basic.HDFSConnection;

public class Main {

	public static void main(String[] args) {
		
		HDFSConnection con = new HDFSConnection();
		FileSystem hdfs = con.getFs();
		
		Path file = new Path(IFootballDataConstants.PLAYER_ATTRIBUTES);
		
		SparkBuilder sBuilder = new SparkBuilder();
				
		SparkConf conf = 
				sBuilder.buildSparkConf();
				
		SparkSession session = sBuilder.buildSession();
		
		//Read data set from hdfs
		Dataset<Row> player = FileReaderFromHDFS.readCSVFromHDFSToSpark(
				session, sBuilder.getContext(), 
				IFootballDataConstants.PLAYER_ATTRIBUTES);
		
		//Print Schema of our dataset
		player.printSchema();
		
		//get first 10 Lines of player
		player.show(10);
		
		player.createOrReplaceTempView("player_attributes");
		
		Dataset<Row> testdata = session.sql("select * from player_attributes "
				+ "where Potential is not null "
				+ "limit 10");
		
		try {
			if(!hdfs.exists(new Path("../testdata.txt"))) {
				testdata.javaRDD().saveAsTextFile("../testdata.txt");
			}
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		
		Dataset<Row> playerSortedOnPotential = session.sql("select * from player_attributes"
				+ " where overall_rating is not null").sort(col("overall_rating").desc());
		playerSortedOnPotential.show(10);
	}

}
