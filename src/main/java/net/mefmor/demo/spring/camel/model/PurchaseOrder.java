package net.mefmor.demo.spring.camel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "PurchaseOrder")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseOrder {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private double price;

    @XmlAttribute
    private double amount;

    @XmlPath("info[@type='author']/@value")
    private String author;

    @XmlPath("info[@type='ISBN'][@key='13']/@value")
    private String isbn;


    @XmlAttribute(name = "other")
    @XmlJavaTypeAdapter(OtherAdapter.class)
    private Other other;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Other {
        private String genre;
        private String contentRate;
    }

    private static class OtherAdapter extends XmlAdapter<String, Other> {
        @Override
        public Other unmarshal(String v) {
            return Other.builder().genre(v.substring(1, 2)).contentRate(v.substring(2, 4)).build();
        }

        @Override
        public String marshal(Other v) {
            return "A" + v.getGenre();
        }
    }
}
