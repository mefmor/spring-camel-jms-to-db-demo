package net.mefmor.demo.spring.camel;

import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class JmsToDbRouteTest {
    @Autowired
    private CamelContext camelContext;

    @EndpointInject(uri = "mock:a")
    private MockEndpoint mockA;

    @EndpointInject(uri = "mock:b")
    private MockEndpoint mockB;

    @Produce(uri = "direct:start")
    private ProducerTemplate start;

    @Test
    public void testPositive() throws Exception {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());

        mockA.expectedBodiesReceived("David");
        mockB.expectedBodiesReceived("Hello David");

        start.sendBody("David");

        MockEndpoint.assertIsSatisfied(camelContext);
    }

}