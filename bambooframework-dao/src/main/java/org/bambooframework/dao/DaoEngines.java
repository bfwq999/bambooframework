package org.bambooframework.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bambooframework.core.exception.BambooIllegalArgumentException;
import org.bambooframework.core.util.IoUtil;
import org.bambooframework.dao.impl.DaoEngineConfigurationImpl;
import org.bambooframework.dao.impl.DaoEngineInfoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DaoEngines {
	private static Logger log = LoggerFactory.getLogger(DaoEngines.class);

	public static final String NAME_DEFAULT = "default";

	protected static boolean isInitialized = false;

	protected static Map<String, DaoEngine> daoEngines = new HashMap<String, DaoEngine>();
	protected static Map<String, DaoEngineInfo> daoEngineInfosByName = new HashMap<String, DaoEngineInfo>();
	protected static Map<String, DaoEngineInfo> daoEngineInfosByResourceUrl = new HashMap<String, DaoEngineInfo>();
	protected static List<DaoEngineInfo> daoEngineInfos = new ArrayList<DaoEngineInfo>();

	public static boolean isInitialized() {
		return isInitialized;
	}

	public static void setInitialized(boolean isInitialized) {
		DaoEngines.isInitialized = isInitialized;
	}

	public static List<DaoEngineInfo> getDaoEngineInfos(){
		return daoEngineInfos;
	}
	
	public static DaoEngine getDaoEngine(String engineName){
		return daoEngines.get(engineName);
	}
	
	public static DaoEngine getDefaultDaoEngine(){
		return daoEngines.get(NAME_DEFAULT);
	}
	
	public synchronized static void init() {
		if (!isInitialized()) {
			if (daoEngines == null) {
				// Create new map to store process-engines if current map is
				// null
				daoEngines = new HashMap<String, DaoEngine>();
			}
			ClassLoader classLoader = getClassLoader();
			Enumeration<URL> resources = null;
			try {
				resources = classLoader.getResources("bamboo.cfg.xml");
			} catch (IOException e) {
				throw new BambooIllegalArgumentException(
						"problem retrieving bamboo.cfg.xml resources on the classpath: "
								+ System.getProperty("java.class.path"), e);
			}

			// Remove duplicated configuration URL's using set. Some
			// classloaders may return identical URL's twice, causing duplicate
			// startups
			Set<URL> configUrls = new HashSet<URL>();
			while (resources.hasMoreElements()) {
				configUrls.add(resources.nextElement());
			}
			for (Iterator<URL> iterator = configUrls.iterator(); iterator
					.hasNext();) {
				URL resource = iterator.next();
				log.info(
						"Initializing process engine using configuration '{}'",
						resource.toString());
				initDaoEnginFromResource(resource);
			}

			setInitialized(true);
		} else {
			log.info("Process engines already initialized");
		}
	}

	private static DaoEngineInfo initDaoEnginFromResource(URL resourceUrl) {

		DaoEngineInfo daoEngineInfo = daoEngineInfosByResourceUrl
				.get(resourceUrl.toString());
		// if there is an existing process engine info
		if (daoEngineInfo != null) {
			// remove that process engine from the member fields
			daoEngineInfos.remove(daoEngineInfo);
			if (daoEngineInfo.getException() == null) {
				String daoEngineName = daoEngineInfo.getName();
				daoEngineInfos.remove(daoEngineName);
				daoEngineInfosByName.remove(daoEngineName);
			}
			daoEngineInfosByResourceUrl.remove(daoEngineInfo.getResourceUrl());
		}

		String resourceUrlString = resourceUrl.toString();
		try {
			log.info("initializing process engine for resource {}", resourceUrl);
			DaoEngine processEngine = buildProcessEngine(resourceUrl);
			String processEngineName = processEngine.getName();
			log.info("initialised process engine {}", processEngineName);
			daoEngineInfo = new DaoEngineInfoImpl(processEngineName,
					resourceUrlString, null);
			daoEngines.put(processEngineName, processEngine);
			daoEngineInfosByName.put(processEngineName, daoEngineInfo);
		} catch (Throwable e) {
			log.error("Exception while initializing process engine: {}",
					e.getMessage(), e);
			daoEngineInfo = new DaoEngineInfoImpl(null, resourceUrlString,
					getExceptionString(e));
		}
		daoEngineInfosByResourceUrl.put(resourceUrlString,
				daoEngineInfo);
		daoEngineInfos.add(daoEngineInfo);
		return daoEngineInfo;
	}

	private static DaoEngine buildProcessEngine(URL resource) {
		InputStream inputStream = null;
		try {
			inputStream = resource.openStream();
			DaoEngineConfigurationImpl daoEngineConfiguration = DaoEngineConfigurationImpl
					.createDaoEngineConfigurationFromInputStream(inputStream);
			return daoEngineConfiguration.buildDaoEngine();

		} catch (IOException e) {
			throw new BambooIllegalArgumentException(
					"couldn't open resource stream: " + e.getMessage(), e);
		} finally {
			IoUtil.closeSilently(inputStream);
		}
	}

	private static String getExceptionString(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/** closes all process engines.  This method should be called when the server shuts down. */
	  public synchronized static void destroy() {
	    if (isInitialized()) {
	      Map<String, DaoEngine> engines = new HashMap<String, DaoEngine>(daoEngines);
	      daoEngines = new HashMap<String, DaoEngine>();
	      
	      for (String processEngineName: engines.keySet()) {
	    	  DaoEngine processEngine = engines.get(processEngineName);
	        try {
	          processEngine.close();
	        } catch (Exception e) {
	          log.error("exception while closing {}", (processEngineName==null ? "the default process engine" : "process engine "+processEngineName), e);
	        }
	      }
	      
	      daoEngineInfosByName.clear();
	      daoEngineInfosByResourceUrl.clear();
	      daoEngineInfos.clear();
	      
	      setInitialized(false);
	    }
	  }
}
