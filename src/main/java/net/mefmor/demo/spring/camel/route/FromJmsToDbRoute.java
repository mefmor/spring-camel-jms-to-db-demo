package net.mefmor.demo.spring.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FromJmsToDbRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("{{incoming.from.uri}}")
                .unmarshal().jaxb("net.mefmor.demo.spring.camel.model")
                .to("{{outgoing.to.database.uri}}");
    }
}
