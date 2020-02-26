package net.mefmor.demo.spring.camel;

import lombok.SneakyThrows;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;


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

    @Value("classpath:data/single_purchase_order.xml")
    private Resource singlePurchaseOrder;

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
        mock.expectedBodiesReceived(new PurchaseOrder("Camel in Action", 6999.0, 1.0));

        template.sendBody(asString(singlePurchaseOrder));

        mock.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void xmlWithPartialSetParametersSuccessfullyConvertedWithDefaultFieldValues() throws InterruptedException {
        mock.expectedBodiesReceived(PurchaseOrder.builder().name("Camel in Action").build());

        template.sendBody("<purchaseOrder name=\"Camel in Action\"/>");

        mock.assertIsSatisfied();
    }

    @Test(expected = org.apache.camel.CamelExecutionException.class)
    @DirtiesContext
    public void incorrectXmlWillThrownException() {
        template.sendBody("Incorrect XML");
    }

    @SneakyThrows
    private String asString(Resource resource) {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
    }

}
