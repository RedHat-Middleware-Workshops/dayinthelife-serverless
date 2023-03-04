// MeterConsumer.java updated for Kamel 1.8.2
// Last update: March 04, 2023

// Maven dependencies used in this Camel route
// camel-k: dependency=camel:jdbc
// camel-k: dependency=mvn:io.quarkus.quarkus-kubernetes-config
// camel-k: dependency=mvn:io.quarkus:quarkus-jdbc-postgresql

// OpenShift secrets used in this Camel route
// camel-k: config=secret:rh-cloud-services-service-account
// camel-k: config=secret:pg-login

// Configuration files used in this Camel route
// camel-k: config=file:meters.properties

// OpenAPI file used to create the exposed route.
// camel-k: open-api=file:openapi-spec.yaml

// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;

public class MeterConsumer extends RouteBuilder {
    @Override 
    public void configure() throws Exception {
        // This direct:create is called from line 20 on the openapi-spec.yaml file "operationId: create"
        from("direct:create").routeId("MetersFromAPI")
            .throttle(3).timePeriodMillis(20000)
            // Convert received JSON data into Java objects, reference: https://camel.apache.org/components/3.20.x/eips/marshal-eip.html
            .unmarshal().json()
            // Set headers from data sent by HTTP payload (your curl statement)
            .setHeader("meterId",simple("${body.[value][meterId]}"))
            .setHeader("status",simple("${body.[value][status]}"))
            // Construct SQL INSERT statement
            .setBody(simple("INSERT INTO meter_update(meter_id, timestamp, status_text) VALUES ('${headers.meterId}', to_timestamp(${body.[value][timestamp]}), '${headers.status}');"))
            .log("SQL INSERT statement: ${body}")
            // Send SQL INSERT statement PostGreSQL
            .to("jdbc:default")
            // Construct SQL SELECT statement
            .setBody(simple("SELECT address, id as meter_id, '${headers.status}' as status_text , latitude, longitude FROM meter where id = '${headers.meterId}' ;"))
            .log("SQL SELECT statement: ${body}")
            // Send SQL SELECT statement to PostGreSQL
            .to("jdbc:default")
            // Convert Java objects back to JSON, reference: https://camel.apache.org/components/3.20.x/eips/marshal-eip.html
            .marshal().json()
            // Send JSON data from PostGreSQL statement to Kafka cluster
            .to("kafka:{{producer.topic}}");
    }
}

