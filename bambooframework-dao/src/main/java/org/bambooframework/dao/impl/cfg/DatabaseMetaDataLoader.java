package org.bambooframework.dao.impl.cfg;

import org.bambooframework.dao.impl.DaoEngineConfigurationImpl;

public interface DatabaseMetaDataLoader {
	
	void setDaoEngineConfiguration(DaoEngineConfigurationImpl daoEngineConfiguration);
	
	void loadDatabaseMetadata();
}
