package producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.Properties;

public class NetflixCsvProducer {

    public static void main(String[] args) throws Exception {
    	
        // KAFKA producer config
        Properties properties = new Properties();
        
        properties.put("bootstrap.servers", "localhost:9092");
        
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        String topic = "movie";
        
      
        Producer<String, String> producer = new KafkaProducer<>(properties);

        // Read csv data from Netflx file and publish to KAFKA topic

        try (CSVReader csvReader = new CSVReader(new FileReader("BestMoviesNetflix.csv"))) {
          
        	String[] csvline;
          
        	while ((csvline = csvReader.readNext()) != null) {

                String msg = String.join(",", csvline);
                
                ProducerRecord<String, String> packet = new ProducerRecord<>(topic, msg);
                
                producer.send(packet);
                
                System.out.println("Sent Packet to Kafka ...." + packet.value());
                
                Thread.sleep(2000);
            }
        }


        producer.close();
    }
}
