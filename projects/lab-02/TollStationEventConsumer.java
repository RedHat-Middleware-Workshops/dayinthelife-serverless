// camel-k: language=java build-property=quarkus.datasource.camel.db-kind=postgresql property=file:tollstationeventconsumer.properties dependency=mvn:io.quarkus:quarkus-jdbc-postgresql:2.0.0.Final config=secret:rh-cloud-services-service-account

import org.apache.camel.builder.RouteBuilder;

public class TollStationEventConsumer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        log.info("About to start route: Kafka Server -> JDBC");

        from("kafka:{{consumer.topic}}?"
                + "topicIsPattern={{consumer.topic.isPattern}}"
                + "&seekTo={{consumer.seekTo}}"
                + "&groupId={{consumer.group}}")
                .routeId("TollStationEventsFromKafka")
                .unmarshal().json()
                .log("Kafka message body: ${body}")
                .setBody(simple("INSERT INTO toll_station_events (toll_station, license_plate, lp_status, lp_timestamp) VALUES ('${body[station]}', '${body[licenseplate]}', '${body[status]}', '${body[timestamp]}') RETURNING id;"))
                .to("jdbc:camel");
    }
}