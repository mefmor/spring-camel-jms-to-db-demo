package net.mefmor.demo.spring.camel;

import lombok.SneakyThrows;
import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;

class DataUtils {
    @SneakyThrows
    static String asString(String pathToResource) {
        return FileCopyUtils.copyToString(new InputStreamReader(DataUtils.class.getResourceAsStream(pathToResource)));
    }

    static PurchaseOrder bookWithFullSetOfParameters() {
        return new PurchaseOrder("Camel in Action", 6999.0, 1.0, "Claus Ibsen, Jonathan Anstey", "978-0-13-235088-4");
    }

    static PurchaseOrder bookWithPartialParameters() {
        return PurchaseOrder.builder().name("Clean code").price(9666.0).amount(2.0).build();
    }
}
