package org.bambooframework.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfigurer {
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean(ApplicationContext ctx,DataSource dataSource) throws IOException{
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setMapperLocations(ctx.getResources("classpath:db/mapping/*.mapper.xml"));
		sqlSessionFactory.setPlugins(new Interceptor[]{new PageInterceptor(),idGeneratorInterceptor()});
		return sqlSessionFactory;
	}
	
	@Bean
	public IdGeneratorInterceptor idGeneratorInterceptor(){
		return new IdGeneratorInterceptor();
	}
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("org.bambooframework.dao");
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
		return configurer;
	}
}
