// camel-k: language=java dependency=mvn:org.postgresql:postgresql:42.2.10 property-file=sender.properties secret=pg-login

import org.apache.camel.builder.RouteBuilder;
import java.util.Random;
import org.apache.camel.Exchange;

public class Sender extends RouteBuilder {
  @Override
  public void configure() throws Exception {


      // Write your routes here, for example:
      from("timer:java?period=1000&repeatCount=1").routeId("Sender")
        .setBody(simple("SELECT extract(epoch from now()) as current_timestamp, id from meter ORDER BY RANDOM() LIMIT 100"))
        .to("jdbc:dataSource")
        .split(body())
          .parallelProcessing().threads(20)
          .setHeader("status").method(this, "genStatus()")
          .setBody(simple("{\"key\": \"${body['id']}\",\"value\": { \"meterId\":\"${body['id']}\",\"timestamp\": ${body['current_timestamp']},\"status\":\"${header.status}\"}}"))
          .log("${body}")
          .setHeader(Exchange.HTTP_METHOD, constant("POST"))
          .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
          .to("http://meter-consumer.user1.svc/meter/status")
          .log("RETURN --> ${body}")
        .end()
        ;

  }

  public String genStatus(){

    String[] status_list = {"unknown", "occupied", "available"};
    Random generator = new Random();
    return status_list[generator.nextInt(status_list.length)];
  }
}
