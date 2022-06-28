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

import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame;

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
				.agg(round(avg("overall_rating"), 2).as("avg_rating"));
		
		//show the 15 best players (we only have id's right now. We need to join later.
		//NIB 062322
		Dataset<Row> top15Players = groupedPlayers.select("*").sort(col("avg_rating").desc()).limit(15);
		top15Players.show();
		
		players.rollup("player_fifa_api_id", "date").count().show();
		
		Dataset<Row> player_basic = FileReaderFromHDFS.readCSVFromHDFSToSpark(
				session,sBuilder.getContext(), IFootballDataConstants.PLAYER);
		player_basic.toDF();
		player_basic.select("*").limit(10).show();
		
		//make a join to get the player names of the best 15 Players.
		 Dataset<Row> top15PlayersWithName = top15Players.join(player_basic, 
				 top15Players.col("player_fifa_api_id")
						 .equalTo(player_basic.col("player_fifa_api_id")), "inner")
				 .drop(top15Players.col("player_fifa_api_id"));
		 top15PlayersWithName.sort(col("avg_rating").desc()).show();
		 
		 
		 
	}
}
