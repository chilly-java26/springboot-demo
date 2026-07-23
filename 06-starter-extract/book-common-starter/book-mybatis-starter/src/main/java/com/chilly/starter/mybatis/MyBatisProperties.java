package com.chilly.starter.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybatis")
public class MyBatisProperties {
    private String typeAliasesPackage;
    private String[] mapperLocations;

    public String getTypeAliasesPackage() { return typeAliasesPackage; }
    public void setTypeAliasesPackage(String typeAliasesPackage) { this.typeAliasesPackage = typeAliasesPackage; }
    public String[] getMapperLocations() { return mapperLocations; }
    public void setMapperLocations(String[] mapperLocations) { this.mapperLocations = mapperLocations; }
}