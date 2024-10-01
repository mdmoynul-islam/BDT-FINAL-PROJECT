# Netflix Data Streaming and Visualization Project

This project demonstrates the setup of Kafka for streaming Netflix data and its integration with HBase, Spark, and Python for data visualization.

## Prerequisites

- Kafka installed in the `~/kafka` directory.
- HBase installed and configured.
- Spark installed.
- Java installed for running JAR files.
- Python installed (Anaconda optional).
- Python IDE such as VS Code installed.

## Steps to Set Up and Run the Services

### 1. Run Zookeeper

1. Open a terminal and navigate to the Kafka folder:
    ```bash
    cd ~/kafka
    ```
2. Start Zookeeper using the following command:
    ```bash
    bin/zookeeper-server-start.sh config/zookeeper.properties
    ```

### 2. Run Kafka Broker

1. Open a new terminal and navigate to the Kafka folder:
    ```bash
    cd ~/kafka
    ```
2. Start the Kafka broker:
    ```bash
    bin/kafka-server-start.sh config/server.properties
    ```

### 3. Run HBase Service

1. Open a new terminal and run the following commands:
    ```bash
    sudo service hbase-master start
    sudo service hbase-regionserver start
    ```

### 4. Create the Movie Table in HBase

1. Open a new terminal and start the HBase shell:
    ```bash
    hbase shell
    ```
2. Create the `movies` table with the necessary column families:
    ```bash
    create 'movies', 'info', 'stats'
    ```
3. Verify the table creation:
    ```bash
    desc 'movies'
    ```

### 5. Run Netflix Stream Producer JAR File

1. Open a new terminal and navigate to the folder containing the Netflix Stream Producer JAR file:
    ```bash
    cd <Jar File folder>
    ```
2. Run the JAR file:
    ```bash
    java -jar NetflixStreamProducer.jar
    ```

### 6. Run Netflix Stream Consumer JAR File

1. Open a new terminal and navigate to the folder containing the Netflix Stream Consumer JAR file:
    ```bash
    cd <Jar File folder>
    ```
2. Run the JAR file using Spark:
    ```bash
    spark-submit --class "consumer.NetflixConsumerSpark" --master local[*] NetflixStreamConsumerSpark.jar
    ```

### 7. Data Analysis and Visualization

1. Install Python (Anaconda optional) and VS Code (or any other Python IDE).
2. Open the `NetflixMovieDataVizulation.ipynb` notebook located in the `DataVisualization` folder using VS Code.
3. Open an integrated terminal in VS Code and install the required Python packages:
    ```bash
    pip install happybase setuptools pandas matplotlib seaborn plotly nbformat
    ```
4. Run the notebook for data analysis and visualization.

## Tools and Libraries

- **Kafka**: For streaming data.
- **HBase**: For storing and retrieving movie data.
- **Spark**: For processing streamed data.
- **Python**: For data analysis and visualization (using libraries like pandas, matplotlib, seaborn, plotly).

