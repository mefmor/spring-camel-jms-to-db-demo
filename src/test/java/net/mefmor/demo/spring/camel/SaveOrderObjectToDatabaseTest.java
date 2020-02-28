package net.mefmor.demo.spring.camel;

import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@Sql(scripts = "/hsqldb/init.sql")
@SqlConfig(separator = "/;")
@DirtiesContext
public class SaveOrderObjectToDatabaseTest {

    @Autowired
    private CamelContext context;

    @Produce(uri = "direct:routeStart")
    private ProducerTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setupRoute() throws Exception {

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:routeStart").to("{{outgoing.to.database.uri}}");
            }
        });
    }

    @Test
    @DirtiesContext
    public void purchaseOrderInstanceShouldBeSavedInDatabase() {
        PurchaseOrder incomingObject = new PurchaseOrder("Camel in Action", 6999.0, 1.0);

        template.sendBody(incomingObject);
        final List<PurchaseOrder> outputOrders = jdbcTemplate.query("select * from customers", BeanPropertyRowMapper.newInstance(PurchaseOrder.class));

        assertThat(outputOrders.size(), is(1));
        assertThat(outputOrders.get(0), is(incomingObject));
    }
}