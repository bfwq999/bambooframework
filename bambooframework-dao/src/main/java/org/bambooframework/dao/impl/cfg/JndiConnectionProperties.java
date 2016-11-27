package org.bambooframework.dao.impl.cfg;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.bambooframework.core.exception.BambooException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JndiConnectionProperties extends ConnectionProperties {
	static Logger log = LoggerFactory.getLogger(JndiConnectionProperties.class);
	protected String jndiName;
	protected DataSource dataSource;
	
	@Override
	public DataSource getDataSource() {
		if(dataSource != null){
			return dataSource;
		}
		if(log.isDebugEnabled()){
			log.debug("initializing datasource by jndiName: ", jndiName);
		}
		try {
			dataSource = (DataSource) new InitialContext()
			.lookup(jndiName);
		} catch (NamingException e) {
			throw new BambooException(
						"couldn't lookup datasource from "
								+ jndiName + ": "
								+ e.getMessage(), e);
		}
		return dataSource;
	}
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

}
