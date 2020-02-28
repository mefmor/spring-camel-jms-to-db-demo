package net.mefmor.demo.spring.camel;

import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Arrays;
import java.util.List;

import static net.mefmor.demo.spring.camel.DataUtils.asString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@Sql(scripts = "/hsqldb/init.sql")
@SqlConfig(separator = "/;")
@DirtiesContext
public class XmlToDatabaseTest {
    @Produce(uri = "{{incoming.from.uri}}")
    private ProducerTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Ignore("TODO: Add to the route the ability to process a single object")
    @DirtiesContext
    public void informationFromPurchaseOrderXmlShouldBeSavedToDatabase() {
        template.sendBody(asString("/data/single_purchase_order.xml"));
        final List<PurchaseOrder> outputOrders = jdbcTemplate.query("select * from customers", BeanPropertyRowMapper.newInstance(PurchaseOrder.class));

        assertThat(outputOrders.size(), is(1));
        assertThat(outputOrders.get(0), is(new PurchaseOrder("Camel in Action", 6999.0, 1.0)));
    }

    @Test
    @DirtiesContext
    public void purchaseOrdersFromXmlShouldBeSavedToDatabase() {
        final List<PurchaseOrder> expectedOrders =
                Arrays.asList(new PurchaseOrder("Clean code", 9666.0, 2.0),
                new PurchaseOrder("Camel in Action", 6999.0, 1.0));

        template.sendBody(asString("/data/order_list_with_two_orders.xml"));
        final List<PurchaseOrder> outputOrders = jdbcTemplate.query("select * from customers", BeanPropertyRowMapper.newInstance(PurchaseOrder.class));

        assertThat(outputOrders, equalTo(expectedOrders));
    }

}
