package net.mefmor.demo.spring.camel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "Orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class Orders {
    @XmlElementWrapper(name = "List")
    @XmlElement(name = "PurchaseOrder")
    private List<PurchaseOrder> orderList;
}
