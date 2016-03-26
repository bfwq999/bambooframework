package org.bambooframework.config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(DatabaseConfigurer.class)
public class DatabaseInit implements ApplicationContextAware {
	private static final Logger log = LoggerFactory
			.getLogger(DatabaseInit.class);

	@Autowired
	DataSource dataSource;
	
	@Autowired
	ApplicationContext ctx;
	
	@PostConstruct
	public void initDatabase() throws Exception {
		exeSql("classpath:db/h2/create-tables.sql", dataSource);
		exeSql("classpath:db/h2/init-data.sql", dataSource);
		exeSql("classpath:db/h2/sample-data.sql", dataSource);
	}
	
	protected void exeSql(String file,DataSource dataSource) throws Exception{
		String sqlStatement = null;
		Connection connection  = null;
		try {
			connection = dataSource.getConnection();
			Exception exception = null;
			
			String ddlStatements = readFileAsString(ctx.getResource(file).getFile());

			BufferedReader reader = new BufferedReader(new StringReader(
					ddlStatements));
			String line = readNextTrimmedLine(reader);
			while (line != null) {
				if (line.startsWith("/*")) {
					
				} else if (line.startsWith("-- ")) {
					
				} else if (line.length() > 0) {
					if (line.endsWith(";")) {
						sqlStatement = addSqlStatementPiece(sqlStatement,
								line.substring(0, line.length() - 1));

						Statement jdbcStatement = connection.createStatement();
						try {
							// no logging needed as the connection will log it
							jdbcStatement.execute(sqlStatement);
							jdbcStatement.close();
						} catch (Exception e) {
							log.error("problem during statement {}",sqlStatement, e);
							throw e;
						} finally {
							sqlStatement = null;
						}
					} else {
						sqlStatement = addSqlStatementPiece(sqlStatement, line);
					}
				}

				line = readNextTrimmedLine(reader);
			}
			log.info("exe successful");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected String readNextTrimmedLine(BufferedReader reader)
			throws IOException {
		String line = reader.readLine();
		if (line != null) {
			line = line.trim();
		}
		return line;
	}

	public static String readFileAsString(File file) {
		byte[] buffer = new byte[(int) file.length()];
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			inputStream.read(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ignore) {
				}
			}
		}
		return new String(buffer);
	}

	protected String addSqlStatementPiece(String sqlStatement, String line) {
		if (sqlStatement == null) {
			return line;
		}
		return sqlStatement + " \n" + line;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
	}
}
