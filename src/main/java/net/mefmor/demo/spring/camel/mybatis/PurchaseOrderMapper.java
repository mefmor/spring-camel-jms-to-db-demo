package net.mefmor.demo.spring.camel.mybatis;

import net.mefmor.demo.spring.camel.model.PurchaseOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseOrderMapper {
    @Insert("INSERT INTO customers(name, price, amount, author, isbn, genre, content_rate) VALUES (#{name,jdbcType=VARCHAR}, "
            + "#{price,jdbcType=DOUBLE}, #{amount,jdbcType=DOUBLE}, #{author,jdbcType=VARCHAR}, "
            + "#{isbn,jdbcType=VARCHAR}, #{other.genre,jdbcType=VARCHAR}, #{other.contentRate,jdbcType=VARCHAR})")
    void insertTrade(PurchaseOrder purchaseOrder);
}
