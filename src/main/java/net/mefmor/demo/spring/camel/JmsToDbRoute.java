package net.mefmor.demo.spring.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JmsToDbRoute extends RouteBuilder {
    @Override
    public void configure() {
        from("direct:start")
                .to("mock:a")
                .transform(simple("Hello ${body}"))
                .to("mock:b");
    }
}
