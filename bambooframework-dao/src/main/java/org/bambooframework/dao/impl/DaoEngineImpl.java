/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bambooframework.dao.impl;

import org.bambooframework.dao.DaoEngine;
import org.bambooframework.dao.DaoEngineConfiguration;
import org.bambooframework.dao.QueryService;
import org.bambooframework.dao.impl.interceptor.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoEngineImpl implements DaoEngine {

	private static Logger log = LoggerFactory.getLogger(DaoEngineImpl.class);

	protected String name;
	protected CommandExecutor commandExecutor;
	protected DaoEngineConfiguration daoEngineConfiguration;
	protected QueryService queryService;

	public DaoEngineImpl(DaoEngineConfiguration daoEngineConfiguration) {
		this.name = daoEngineConfiguration.getDaoEngineName();
		this.daoEngineConfiguration = daoEngineConfiguration;
		this.commandExecutor = daoEngineConfiguration.getCommandExecutor();
		queryService = new QueryServiceImpl(
				daoEngineConfiguration.getCommandExecutor());
	}

	public void close() {
	}

	public String getName() {
		return name;
	}

	@Override
	public QueryService getQueryService() {
		return queryService;
	}

	@Override
	public DaoEngineConfiguration getDaoEngineConfiguration() {
		return daoEngineConfiguration;
	}
}
