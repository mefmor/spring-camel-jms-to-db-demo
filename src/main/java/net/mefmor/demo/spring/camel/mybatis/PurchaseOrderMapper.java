package net.mefmor.demo.spring.camel.mybatis;

import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseOrderMapper {
    @Insert("INSERT INTO customers(name, price, amount) VALUES (#{name,jdbcType=VARCHAR}, #{price,jdbcType=DOUBLE}, #{amount,jdbcType=DOUBLE})")
    void insertTrade(PurchaseOrder purchaseOrder);
}
