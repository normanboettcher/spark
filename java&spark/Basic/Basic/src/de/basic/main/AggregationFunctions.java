package de.basic.main;

import org.apache.spark.SparkConf;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import de.basic.util.FileReaderFromHDFS;
import de.basic.util.IFootballDataConstants;
import de.basic.util.SparkBuilder;

import static org.apache.spark.sql.functions.*;

public class AggregationFunctions {
	
	public static void main(String[] args) {
		SparkBuilder sBuilder = new SparkBuilder();
		SparkConf conf = sBuilder.buildSparkConf();
		SparkSession session = sBuilder.buildSession();
		
		Dataset<Row> team_attributes = FileReaderFromHDFS.readCSVFromHDFSToSpark(
				session, sBuilder.getContext(), IFootballDataConstants.TEAM_ATTRIBUTES);
		
		team_attributes.printSchema();
		
		Dataset<Row> players = FileReaderFromHDFS.readCSVFromHDFSToSpark(
				session, sBuilder.getContext(), IFootballDataConstants.PLAYER_ATTRIBUTES);
		
		players.toDF();
		players.select(col("*")).agg(count("player_fifa_api_id")).show();
		
		//show distinct player_ids
		players.select(count("player_fifa_api_id")).show();
		
		//now with approx_count
		players.select(col("*")).agg(approx_count_distinct("player_Fifa_api_id")).show();
		
		//smalles rating over all players
		players.select(min("overall_rating")).show();
		
		//looking for avg rating over all players
		players.select(avg("overall_rating")).show();
		
		//kurtosis
		players.select(kurtosis("overall_rating")).show();
		
		//skewness
		players.select(skewness("overall_rating")).show();
		
		//variance
		players.select(var_pop("overall_rating")).show();
		
		//std
		players.select(stddev("overall_rating")).show();
		
		//Covariance between overall_rating and potential
		players.select(covar_pop("overall_rating", "potential")).show();
		
		//since we have some player ids multiple times, do a groupby and a avg
		Dataset<Row> groupedPlayers = players.select("*").groupBy("player_fifa_api_id")
				.agg(avg("overall_rating").as("avg_rating"));
		
		//show the 15 best players (we only have id's right now. We need to join later.
		//NIB 062322
		groupedPlayers.select("*").sort(col("avg_rating").desc()).limit(15).show();
		
		players.rollup("overall_rating", "date").count().show();
	}
}
