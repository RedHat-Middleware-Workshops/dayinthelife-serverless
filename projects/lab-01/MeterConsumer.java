// camel-k: language=java open-api=openapi-spec.yaml dependency=camel-openapi-java dependency=mvn:org.postgresql:postgresql:42.2.10 property=meters.properties resource=secret:pg-login resource=secret:rh-cloud-services-service-account

import org.apache.camel.builder.RouteBuilder;


public class MeterConsumer extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:create").routeId("MetersFromAPI")
            .throttle(3).timePeriodMillis(20000)
            .unmarshal().json()
            .setHeader("meterId",simple("${body.[value][meterId]}"))
            .setHeader("status",simple("${body.[value][status]}"))
            .setBody(simple("INSERT INTO meter_update(meter_id, timestamp, status_text) VALUES ('${headers.meterId}', to_timestamp(${body.[value][timestamp]}), '${headers.status}');"))
            .log("SQL INSERT statement: ${body}")
            .to("jdbc:dataSource")
            .setBody(simple("SELECT address, id as meter_id, '${headers.status}' as status_text , latitude, longitude FROM meter where id = '${headers.meterId}' ;"))
            .log("SQL SELECT statement: ${body}")
            .to("jdbc:dataSource")
            .marshal().json()
            .to("kafka:{{producer.topic}}");



    }
}
