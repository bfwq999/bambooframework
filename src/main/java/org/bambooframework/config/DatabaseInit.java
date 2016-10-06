package org.bambooframework.config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.bambooframework.common.CodeConstants;
import org.bambooframework.common.IdGenerator;
import org.bambooframework.sql.DatabaseUtils;
import org.bambooframework.sql.metadata.Column;
import org.bambooframework.sql.metadata.Database;
import org.bambooframework.sql.metadata.Table;
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

		initMetadata(dataSource, null, null);
		loadDbMetadata(dataSource);
		DatabaseUtils.addDataSource(dataSource);
	}

	private void initMetadata(DataSource dataSource, String catalogFilter,
			String schemaFilter) throws SQLException {
		Database database = new Database();
		Connection conn = null;
		ResultSet rs = 		null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
			
			try {
				rs = dbmd.getColumns(catalogFilter, schemaFilter, null, null);
				while (rs.next()) {
					String catalogName = rs.getString("TABLE_CAT");
					String schemaName = rs.getString("TABLE_SCHEM");
					String tableName = rs.getString("TABLE_NAME");
					String columnName = rs.getString("COLUMN_NAME");
					int dataType = Integer.parseInt(rs.getString("DATA_TYPE"));
					Table table = database.getTable(tableName);
					if (table == null) {
						table = new Table(database);
						table.setName(tableName);
						table.setCatalog(catalogName);
						table.setSchema(schemaName);
						database.addTable(table);
					}
					Column col = new Column(table);
					col.setName(columnName);
					col.setType(dataType);
					table.addColumn(col);
				}
			} finally {
				if (rs != null)
					rs.close();
			}

			try {
				String[] tableNames = database.getTableNames();
				for (int i = 0; i < tableNames.length; i++) {
					Table table = database.getTable(tableNames[i]);
					rs = dbmd.getPrimaryKeys(catalogFilter, schemaFilter,
							table.getName());
					if (rs.next()) {
						String columnName = rs.getString("COLUMN_NAME");
						Column col = table.getColumn(columnName);
						col.setPrimaryKey(CodeConstants.YESNO_Y);
						table.setPrimaryKey(col);
					}
				}
			} finally {
				if (rs != null)
					rs.close();
			}
			
			Statement stmt = conn.createStatement();
			Integer dbId = IdGenerator.getNextId();
			stmt.executeUpdate("insert into DB_DATABASE(id_,name_) values("+dbId+",'默认')");
			for(Table table : database.getTables()){
				int tblId = IdGenerator.getNextId();
				table.setId(tblId);
				stmt.executeUpdate("insert into DB_TABLE(id_,name_,db_id_) values("+tblId+",'"+table.getName()+"',"+dbId+")");
				for(Column col:table.getColumns()){
					int colId = IdGenerator.getNextId();
					col.setId(colId);
					stmt.executeUpdate("insert into DB_COLUMN(id_,name_,TABLE_ID_,TYPE_,PRIMARY_KEY_) "
							+ "values("+colId+",'"+col.getName()+"',"+tblId+","+col.getType()+",'"+col.getPrimaryKey()+"')");
				}
			}
			//外键
			try {
				String[] tableNames = database.getTableNames();
				for (int i = 0; i < tableNames.length; i++) {
					Table table = database.getTable(tableNames[i]);
					rs = dbmd.getImportedKeys(catalogFilter, schemaFilter,
							table.getName());
					while(rs.next()) {
						String pkTableName = rs.getString("PKTABLE_NAME");
						String pkColName = rs.getString("PKCOLUMN_NAME");
						String fkTableName = rs.getString("FKTABLE_NAME");
						String fkColName = rs.getString("FKCOLUMN_NAME");
						Column pkCol = database.getTable(pkTableName).getColumn(pkColName);
						Column fkCol = database.getTable(fkTableName).getColumn(fkColName);
						fkCol.setForeignKey(pkCol);
						stmt.executeUpdate("update DB_COLUMN set FOREIGN_KEY_="+pkCol.getId()+" where id_="+fkCol.getId());
					}
				}
			} finally {
				if (rs != null)
					rs.close();
			}
		} finally {
			try {
				conn.commit();
				conn.close();
			} catch (Exception e) { /* ignore */
			}
		}
	}

	protected void loadDbMetadata(DataSource dataSource) throws Exception {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from DB_DATABASE");
			while (rs.next()) {
				int dbId = rs.getInt("ID_");
				String dbName = rs.getString("NAME_");
				Database db = new Database();
				db.setId(dbId);
				db.setName(dbName);
				DatabaseUtils.addDatabases(db);
				Statement tblStmt = connection.createStatement();
				ResultSet tblRs = tblStmt
						.executeQuery("select * from DB_TABLE where DB_ID_="
								+ dbId);
				while (tblRs.next()) {
					int tableId = tblRs.getInt("ID_");
					String tableName = tblRs.getString("NAME_");
					Table table = new Table(db);
					table.setName(tableName);
					table.setId(tableId);
					db.addTable(table);
					Statement colStmt = connection.createStatement();
					ResultSet colRs = colStmt
							.executeQuery("select * from DB_COLUMN where TABLE_ID_="
									+ tableId);
					while (colRs.next()) {
						int colId = colRs.getInt("ID_");
						String colName = colRs.getString("NAME_");
						Column col = new Column(table);
						col.setName(colName);
						col.setId(colId);
						col.setType(colRs.getInt("TYPE_"));
						col.setLength(colRs.getInt("LENGTH_"));
						col.setPrecision(colRs.getInt("PRECISION_"));
						col.setTableId(colRs.getInt("TABLE_ID_"));
						col.setDefaultValue(colRs.getString("DEFAULT_"));
						col.setPrimaryKey(colRs.getString("PRIMARY_KEY_"));
						col.setComment(colRs.getString("COMMENT_"));
						//int foreignKey =  colRs.getInt("FOREIGN_KEY_");
						//col.setForeignKey();
						table.addColumn(col);
						if(CodeConstants.YESNO_Y.equals(colRs.getString("PRIMARY_KEY_"))){
							table.setPrimaryKey(col);
						}
					}
					colRs.close();
					colStmt.close();
				}
				tblRs.close();
				tblStmt.close();
				
				
				//设置外键
				tblStmt = connection.createStatement();
				tblRs = tblStmt
						.executeQuery("select tbl.NAME_ FK_TABLE_NAME_,col.NAME_ FK_COL_NAME_,tbl2.NAME_ TABLE_NAME_,col2.NAME_ "
								+ " from DB_TABLE tbl,DB_TABLE tbl2,DB_COLUMN col,DB_COLUMN col2"
								+ " where tbl.ID_ = col.TABLE_ID_ AND col.ID_=col2.FOREIGN_KEY_  AND tbl2.ID_=col2.TABLE_ID_" 
								+ " AND col2.FOREIGN_KEY_ is not null AND tbl2.DB_ID_="
								+ dbId);
				while (tblRs.next()) {
					String fkTableName = tblRs.getString("FK_TABLE_NAME_");
					String fkColName = tblRs.getString("FK_COL_NAME_");
					String tblName = tblRs.getString("TABLE_NAME_");
					String colName = tblRs.getString("NAME_");
					db.getTable(tblName).getColumn(colName).setForeignKey(db.getTable(fkTableName).getColumn(fkColName));
				}
				tblRs.close();
				tblStmt.close();
			}
			rs.close();
			stmt.close();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	protected void exeSql(String file, DataSource dataSource) throws Exception {
		String sqlStatement = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Exception exception = null;

			String ddlStatements = readFileAsString(ctx.getResource(file)
					.getFile());

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
							log.error("problem during statement {}",
									sqlStatement, e);
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
		} finally {
			if (connection != null) {
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
