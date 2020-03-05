package net.mefmor.demo.spring.camel;

import net.mefmor.demo.spring.camel.model.Orders;
import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;

import static net.mefmor.demo.spring.camel.DataUtils.*;


@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext
public class TransformXmlToObjectTest {

    @Autowired
    private CamelContext context;

    @Produce(uri = "direct:routeStart")
    private ProducerTemplate template;

    @EndpointInject(uri = "mock:result")
    private MockEndpoint mock;

    @Before
    public void setupRoute() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:routeStart")
                        .unmarshal().jaxb("net.mefmor.demo.spring.camel.model")
                        .to("mock:result");
            }
        });
    }

    @Test
    @DirtiesContext
    public void xmlWithFullSetOfParametersWillBeSuccessfullyConverted() throws InterruptedException {
        testRoute(asString("/data/single_purchase_order.xml"),
                bookWithFullSetOfParameters());
    }

    @Test
    @DirtiesContext
    public void xmlWithPartialSetParametersSuccessfullyConvertedWithDefaultFieldValues() throws InterruptedException {testRoute("<PurchaseOrder name=\"Camel in Action\"/>",
                PurchaseOrder.builder().name("Camel in Action").build());
    }

    @Test
    @DirtiesContext
    public void extractPart() throws InterruptedException {
        testRoute("<PurchaseOrder other=\"ASZZA\"/>",
                PurchaseOrder.builder().other(new PurchaseOrder.Other("S", "ZZ")).build());
    }

    @Test
    @DirtiesContext
    public void xmlWithOrdersSuccessfullyConverted() throws InterruptedException {
        testRoute(asString("/data/order_list_with_two_orders.xml"),
                new Orders(Arrays.asList(
                        bookWithPartialParameters(),
                        bookWithFullSetOfParameters())));
    }

    private void testRoute(String inputXml, Object expectedObject) throws InterruptedException {
        mock.expectedBodiesReceived(expectedObject);
        template.sendBody(inputXml);
        mock.assertIsSatisfied();
    }

    @Test(expected = org.apache.camel.CamelExecutionException.class)
    @DirtiesContext
    public void incorrectXmlWillThrownException() {
        template.sendBody("Incorrect XML");
    }

}
