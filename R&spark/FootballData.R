library(SparkR)

sc <- sparkR.session(master = 'local[*]', appName = 'footballdata')
dataPath <- 'hdfs://master:9000/user/norman/data/football/'
player_attributes_file <- paste(dataPath,'player_attributes.csv', sep = "")
player_attributes_file

player_attributes <- read.df(path = player_attributes_file,
                              header = T, sep = ",", source = 'csv')
head(player_attributes)
printSchema(player_attributes)
player_attributes <- collect(
  agg(
    group_by(
      player_attributes, 'player_fifa_api_id'
    ),
    avg_rating = avg(player_attributes$overall_rating)
  )
)
head(player_attributes)

library(ggplot2)
hist(player_attributes$avg_rating)
ratings <- player_attributes$avg_rating
#count na values
nas <- sum(is.na(ratings))
nas
# only two values. drop them.
ratings <- na.omit(ratings)
mean_rating <- mean(ratings)
mean_rating
median(ratings)
library(ggplot2)
ggplot(data=player_attributes, aes(x = avg_rating)) + geom_histogram() + xlab('avg_ratings') 
#stddev_pop(player_attributes$avg_rating)

print('looks like we have normally distributed ratings')
       