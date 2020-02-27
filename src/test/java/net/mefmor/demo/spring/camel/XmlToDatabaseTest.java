package net.mefmor.demo.spring.camel;

import lombok.SneakyThrows;
import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.util.List;

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
    @DirtiesContext
    public void informationFromXmlShouldBeSavedToDatabase() {
        template.sendBody(asString("/data/single_purchase_order.xml"));
        final List<PurchaseOrder> outputOrders = jdbcTemplate.query("select * from customers", BeanPropertyRowMapper.newInstance(PurchaseOrder.class));

        assertThat(outputOrders.size(), is(1));
        assertThat(outputOrders.get(0), is(new PurchaseOrder("Camel in Action", 6999.0, 1.0)));
    }

    @SneakyThrows
    private String asString(String pathToResource) {
        return FileCopyUtils.copyToString(new InputStreamReader(getClass().getResourceAsStream(pathToResource)));
    }
}
