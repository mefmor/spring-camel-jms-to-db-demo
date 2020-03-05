package net.mefmor.demo.spring.camel;

import lombok.SneakyThrows;
import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

class DataUtils {
    @SneakyThrows
    static String asString(String pathToResource) {
        return FileCopyUtils.copyToString(new InputStreamReader(DataUtils.class.getResourceAsStream(pathToResource)));
    }

    static PurchaseOrder bookWithFullSetOfParameters() {
        return PurchaseOrder.builder()
                .name("Camel in Action")
                .price(6999.0)
                .amount(1.0)
                .author("Claus Ibsen, Jonathan Anstey")
                .isbn("978-0-13-235088-4")
                .other(PurchaseOrder.Other.builder().genre("S").contentRate("AL").build())
                .build();
    }

    static PurchaseOrder bookWithPartialParameters() {
        return PurchaseOrder.builder()
                .name("Clean code")
                .price(9666.0)
                .amount(2.0)
                .build();
    }

    public static class PurchaseOrderMapper implements RowMapper<PurchaseOrder> {
        @Override
        public PurchaseOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
            PurchaseOrder order = PurchaseOrder.builder()
                    .name(rs.getString("name"))
                    .price(rs.getDouble("price"))
                    .amount(rs.getDouble("amount"))
                    .author(rs.getString("author"))
                    .isbn(rs.getString("isbn"))
                    .build();

            if (rs.getString("genre") != null || rs.getString("content_rate") != null) {
                order.setOther(PurchaseOrder.Other.builder()
                        .genre(rs.getString("genre"))
                        .contentRate(rs.getString("content_rate"))
                        .build());
            }

            return order;
        }
    }
}
