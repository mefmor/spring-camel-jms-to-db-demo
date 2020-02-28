package net.mefmor.demo.spring.camel;

import lombok.SneakyThrows;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;

class DataUtils {
    @SneakyThrows
    static String asString(String pathToResource) {
        return FileCopyUtils.copyToString(new InputStreamReader(DataUtils.class.getResourceAsStream(pathToResource)));
    }
}
