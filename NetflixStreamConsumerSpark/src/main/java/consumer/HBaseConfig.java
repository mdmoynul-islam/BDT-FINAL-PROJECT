package consumer;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseConfig {

    // HBase configuration instance
    private static Configuration hbaseConfiguration = HBaseConfiguration.create();
    
    // HBase connection instance
    private static Connection hbaseConnection = null;

    // HBase Table Name
    private static final String MOVIES_TABLE_NAME = "movies";

    // HBase Column Families
    private static final byte[] COLUMN_FAMILY_INFO = Bytes.toBytes("info");
    private static final byte[] COLUMN_FAMILY_STATS = Bytes.toBytes("stats");
    
    // Column qualifiers for the "info" column family
    private static final byte[] COLUMN_TITLE = Bytes.toBytes("title");
    private static final byte[] COLUMN_RELEASE_YEAR = Bytes.toBytes("release_year");
    private static final byte[] COLUMN_DURATION = Bytes.toBytes("duration");
    private static final byte[] COLUMN_MAIN_GENRE = Bytes.toBytes("main_genre");
    private static final byte[] COLUMN_MAIN_PRODUCTION = Bytes.toBytes("main_production");

    // Column qualifiers for the "stats" column family
    private static final byte[] COLUMN_SCORE = Bytes.toBytes("score");
    private static final byte[] COLUMN_NUMBER_OF_VOTES = Bytes.toBytes("number_of_votes");

    // Initialize HBase connection
    public static void initializeConnection() throws IOException {
    	
        try {
        	
            hbaseConnection = ConnectionFactory.createConnection(hbaseConfiguration);
            
            Table table = hbaseConnection.getTable(TableName.valueOf(MOVIES_TABLE_NAME));
            
            System.out.println("Connected to table: " + table.getName());
            
        } catch (IOException e) {
        	
            e.printStackTrace();
        }
    }

    // Create HBase table
    public static void createTable() throws IOException {
      //  Configuration configuration = HBaseConfiguration.create();

        try (Connection connection = ConnectionFactory.createConnection(hbaseConfiguration);
        		
            Admin admin = connection.getAdmin()) {

            TableName tableName = TableName.valueOf(MOVIES_TABLE_NAME);

            // Define column families
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
            ColumnFamilyDescriptor infoColumnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(COLUMN_FAMILY_INFO).build();
            ColumnFamilyDescriptor statsColumnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(COLUMN_FAMILY_STATS).build();

            // Build table descriptor
            TableDescriptor tableDescriptor = tableDescriptorBuilder
                    .setColumnFamily(infoColumnFamilyDescriptor)
                    .setColumnFamily(statsColumnFamilyDescriptor)
                    .build();

            System.out.println("Initializing table creation...");

            if (!admin.tableExists(tableName)) {
            	
                System.out.println("Table doesn't exist! Creating table...");
                
                admin.createTable(tableDescriptor);
                
            } else {
            	
                System.out.println("Table already exists.");
            }

            System.out.println("Table setup complete!");
        }
    }

    // Insert movie record into HBase
    public static void insertMovie(Movie movie) throws IOException {
    	
        Table movieTable = hbaseConnection.getTable(TableName.valueOf(MOVIES_TABLE_NAME));

        System.out.print("Inserting movie data... ");
        
        try {
        	
            Put put = new Put(Bytes.toBytes(movie.getTitle()));

            // Insert data into the "info" column family
            put.addColumn(COLUMN_FAMILY_INFO, COLUMN_RELEASE_YEAR, intToBytes(movie.getReleaseYear()));
            put.addColumn(COLUMN_FAMILY_INFO, COLUMN_DURATION, intToBytes(movie.getDuration()));
            put.addColumn(COLUMN_FAMILY_INFO, COLUMN_MAIN_GENRE, Bytes.toBytes(movie.getMainGenre()));
            put.addColumn(COLUMN_FAMILY_INFO, COLUMN_MAIN_PRODUCTION, Bytes.toBytes(movie.getMainProduction()));

            // Insert data into the "stats" column family
            put.addColumn(COLUMN_FAMILY_STATS, COLUMN_SCORE, floatToBytes(movie.getScore()));
            put.addColumn(COLUMN_FAMILY_STATS, COLUMN_NUMBER_OF_VOTES, intToBytes(movie.getNumberOfVotes()));

            movieTable.put(put);
            
            System.out.println("Data insertion complete.");
            
        } finally {
        	
            movieTable.close();
        }
    }

    // Helper method to convert float to byte array
    private static byte[] floatToBytes(float data) {
    	
        return Bytes.toBytes(String.valueOf(data));
    }
    
    // Helper method to convert int to byte array
    private static byte[] intToBytes(int data) {
    	
        return Bytes.toBytes(String.valueOf(data));
    }
    
}