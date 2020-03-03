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
    @XmlJavaTypeAdapter(GenreAdapter.class)
    private String genre;

    private static class GenreAdapter extends XmlAdapter<String, String> {
        @Override
        public String unmarshal(String v) {
            return v.substring(1, 2);
        }

        @Override
        public String marshal(String v) {
            throw new NotImplementedException();
        }
    }
}
