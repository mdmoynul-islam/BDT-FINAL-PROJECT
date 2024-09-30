package consumer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import consumer.HBaseConfig;
import consumer.Movie;
import kafka.serializer.StringDecoder;
import java.util.*;

public class NetflixConsumerSpark {

    public static void main(String[] args) throws InterruptedException {
        // Set the Spark configuration for local execution with all cores
        SparkConf sparkConf = new SparkConf().setAppName("KafkaSparkStreamingApp").setMaster("local[*]");
        
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // Set up a streaming context with a batch interval of 5 seconds
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, Durations.seconds(5));

        // Initialize HBase connection
        try {
        	
            System.out.println("Initializing HBase connection...");
            
            HBaseConfig.initializeConnection();
            
            System.out.println("HBase initialization complete.");
            
        } catch (Exception ex) {
        	
            System.out.println("Failed to initialize HBase connection!");
            
            System.out.println(ex.getMessage());
        }

        // Kafka broker address
        String kafkaBroker = "localhost:9092";
        
        // Kafka topic to consume messages from
        Set<String> kafkaTopics = Collections.singleton("movie");
        
        // Kafka configuration parameters
        Map<String, String> kafkaConfigParams = new HashMap<>();
        kafkaConfigParams.put("bootstrap.servers", kafkaBroker);
        kafkaConfigParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConfigParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConfigParams.put("group.id", "movie-consumer-group");

        // Create a direct Kafka stream for consuming messages
        JavaPairInputDStream<String, String> kafkaStream = KafkaUtils.createDirectStream(
                streamingContext,
                String.class, String.class,
                StringDecoder.class, StringDecoder.class,
                kafkaConfigParams,
                kafkaTopics
        );

        System.out.println("Kafka broker connected, streaming started...");

        // Process each RDD in the stream
        kafkaStream.foreachRDD(rdd -> {
        	
            System.out.println("Processing RDD...");

            rdd.values()
                .filter(record -> record != null && !record.isEmpty())  // Filter out empty or null records
                .map(record -> parseRecordToMovie(record))  // Parse each record into a Movie object
                .filter(movie -> movie != null)  // Filter out invalid Movie objects
                .foreach(movie -> {
                    System.out.println("Inserting Movie into HBase...");
                    System.out.println(movie);

                    try {
                    	
                        HBaseConfig.insertMovie(movie);  // Insert the valid Movie object into HBase
                        
                    } catch (Exception e) {
                    	
                        e.printStackTrace();  // Log any insertion errors
                    }
                });

            System.out.println("RDD processing complete.");
        });

        // Start the streaming context
        streamingContext.start();
        
        streamingContext.awaitTermination();  // Wait for the computation to terminate
    }

    // Parse a CSV string to a Movie object
    public static Movie parseRecordToMovie(String record) {
    	
        // Expected format: title,release_year,score,votes,duration,main_genre,main_production
        try {
            System.out.println("Parsing movie record...");
            
            String[] recordFields = record.split(",");  // Split the record into fields

            // Create and populate a Movie object
            Movie movie = new Movie();
            
            movie.setTitle(recordFields[0]);
            movie.setReleaseYear(Integer.parseInt(recordFields[1]));
            movie.setScore(Float.parseFloat(recordFields[2]));
            movie.setNumberOfVotes(Integer.parseInt(recordFields[3]));
            movie.setDuration(Integer.parseInt(recordFields[4]));
            movie.setMainGenre(recordFields[5]);
            movie.setMainProduction(recordFields[6]);

            return movie;  // Return the parsed Movie object
            
        } catch (Exception ex) {
        	
            System.out.println("Error parsing record. Discarding the invalid record!");
            
            return null;  // Return null for any parsing errors
        }
    }
}
