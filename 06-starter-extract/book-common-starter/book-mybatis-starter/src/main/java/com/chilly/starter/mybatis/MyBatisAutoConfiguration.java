package com.chilly.starter.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass({SqlSessionFactory.class, DataSource.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MyBatisProperties.class)
public class MyBatisAutoConfiguration {

    @Autowired
    private MyBatisProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        
        // 设置 Mapper XML 位置
        if (properties.getMapperLocations() != null && properties.getMapperLocations().length > 0) {
            ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
            List<Resource> resources = new ArrayList<>();
            for (String location : properties.getMapperLocations()) {
                Resource[] locationResources = resourceResolver.getResources(location);
                for (Resource resource : locationResources) {
                    resources.add(resource);
                }
            }
            if (!resources.isEmpty()) {
                factory.setMapperLocations(resources.toArray(new Resource[0]));
            }
        }
        
        // 设置别名包
        if (properties.getTypeAliasesPackage() != null && !properties.getTypeAliasesPackage().isEmpty()) {
            factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }
        
        // 其他配置：驼峰映射
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(configuration);
        
        return factory.getObject();
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}