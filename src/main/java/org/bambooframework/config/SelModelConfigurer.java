package org.bambooframework.config;

import javax.sql.DataSource;

import org.bambooframework.dictionary.SelModelFactory;
import org.bambooframework.dictionary.TableLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SelModelConfigurer {

	@Autowired
	DataSource dataSource;
	
	@Bean(name="selModelFactory")
	public SelModelFactory selModelFactory(){
		SelModelFactory selModelFactory = new SelModelFactory();
		selModelFactory.registerLoader("tb", tableLoader());
		selModelFactory.setDefaultLoader("tb");
		registerLoader(selModelFactory);
		return selModelFactory;
	}
	
	@Bean
	public TableLoader tableLoader(){
		TableLoader tableLoader = new TableLoader(dataSource, "T_CODE_LIST",
				"NAME_", "TEXT_", "VALUE_", "PARENT_", "FILTER_", "SORT_");
		return tableLoader;
	}

	
	/**
	 * 加载其它selModel
	 * @author lei
	 * @param loaders
	 * @date 2015年9月20日
	 * @Description:
	 */
	public void registerLoader(SelModelFactory selModelFactory){
		
	}
}
