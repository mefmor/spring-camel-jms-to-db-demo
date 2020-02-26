package net.mefmor.demo.spring.camel.mybatis;

import org.apache.camel.component.mybatis.MyBatisComponent;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MyBatisConfiguration {
    @Autowired
    private DataSource dataSource;

    @Bean
    public MyBatisComponent mybatis(SqlSessionFactory sqlSessionFactory) {
        final MyBatisComponent myBatisComponent = new MyBatisComponent();
        myBatisComponent.setSqlSessionFactory(sqlSessionFactory);
        return myBatisComponent;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.addMapper(PurchaseOrderMapper.class);
    }
}
