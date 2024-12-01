# flink

## install 
```
https://flink.apache.org/downloads.html

local run
./bin/start-cluster.sh
http://localhost:8081/

```

## hello world
mvn archetype:enerate -DarchetypeGroupId=org.apache.flink -DarchetypeArtifactId=flink-quickstart-java -DarchetypeVersion=1.13.6

```java
public class StreamingJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromElements(1, 2, 3, 4, 5).map(i -> 2 * i).print();

        env.execute();
    }
}
```
mvn package

## doc
https://ci.apache.org/projects/flink/flink-docs-master/zh/