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
        mock.expectedBodiesReceived(bookWithFullSetOfParameters());

        template.sendBody(asString("/data/single_purchase_order.xml"));

        mock.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void xmlWithPartialSetParametersSuccessfullyConvertedWithDefaultFieldValues() throws InterruptedException {
        mock.expectedBodiesReceived(PurchaseOrder.builder().name("Camel in Action").build());

        template.sendBody("<PurchaseOrder name=\"Camel in Action\"/>");

        mock.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void extractPartOfValue() throws InterruptedException {
        mock.expectedBodiesReceived(PurchaseOrder.builder().genre("S").build());

        template.sendBody("<PurchaseOrder other=\"ASAAA\"/>");

        mock.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void xmlWithOrdersSuccessfullyConverted() throws InterruptedException {
        mock.expectedBodiesReceived(new Orders(Arrays.asList(
                bookWithPartialParameters(),
                bookWithFullSetOfParameters())));

        template.sendBody(asString("/data/order_list_with_two_orders.xml"));

        mock.assertIsSatisfied();
    }

    @Test(expected = org.apache.camel.CamelExecutionException.class)
    @DirtiesContext
    public void incorrectXmlWillThrownException() {
        template.sendBody("Incorrect XML");
    }

}
