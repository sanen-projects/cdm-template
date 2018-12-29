package online.sanen.cdm.template.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mhdt.log.Log;


/**
 * 
 *
 * @author LazyToShow <br>
 *         Date: Nov 3, 2018 <br>
 *         Time: 10:52:52 AM
 */
public class JdbcTransaction implements Transaction {

	private static final Log log = Log.getDefaultLog();

	protected Connection connection;
	protected DataSource dataSource;
	protected TransactionIsolationLevel level;
	protected boolean autoCommmit;

	/**
	 * 
	 * @param ds
	 * @param desiredLevel
	 * @param desiredAutoCommit
	 */
	public JdbcTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {

		dataSource = ds;
		level = desiredLevel;
		autoCommmit = desiredAutoCommit;
	}

	/**
	 * 
	 * @param connection
	 */
	public JdbcTransaction(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (connection == null) {
			openConnection();
		}
		
		return connection;
	}

	@Override
	public void commit() throws SQLException {
		
		if (connection != null && !connection.getAutoCommit()) {
			if (log.isDebugEnable()) {
				log.debug("Committing JDBC Connection [" + connection + "]");
			}
			connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		
		if (connection != null && !connection.getAutoCommit()) {
			if (log.isDebugEnable()) {
				log.debug("Rolling back JDBC Connection [" + connection + "]");
			}
			
			connection.rollback();
		}
	}

	@Override
	public void close() throws SQLException {
		
		if (connection != null) {
			resetAutoCommit();
			if (log.isDebugEnable()) {
				log.debug("Closing JDBC Connection [" + connection + "]");
			}
			connection.close();
		}
	}

	protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
		try {
			if (connection.getAutoCommit() != desiredAutoCommit) {
				if (log.isDebugEnable()) {
					log.debug("Setting autocommit to " + desiredAutoCommit + " on JDBC Connection [" + connection + "]");
				}
				connection.setAutoCommit(desiredAutoCommit);
			}
		} catch (SQLException e) {
			// Only a very poorly implemented driver would fail here,
			// and there's not much we can do about that.
			throw new TransactionException("Error configuring AutoCommit.  "
					+ "Your driver may not support getAutoCommit() or setAutoCommit(). " + "Requested setting: "
					+ desiredAutoCommit + ".  Cause: " + e, e);
		}
	}

	protected void resetAutoCommit() {

		try {

			if (!connection.getAutoCommit()) {
				// Cdm does not call commit/rollback on a connection if just selects were
				// performed.
				// Some databases start transactions with select statements
				// and they mandate a commit/rollback before closing the connection.
				// A workaround is setting the autocommit to true before closing the connection.
				// Sybase throws an exception here.
				if (log.isDebugEnable()) {
					log.debug("Resetting autocommit to true on JDBC Connection [" + connection + "]");
				}
				connection.setAutoCommit(true);
			}

		} catch (SQLException e) {
			if (log.isDebugEnable()) {
				log.debug("Error resetting autocommit to true " + "before closing the connection.  Cause: " + e);
			}
		}
	}

	protected void openConnection() throws SQLException {

		if (log.isDebugEnable()) {
			log.debug("Opening JDBC Connection");
		}

		connection = dataSource.getConnection();

		if (level != null) {
			connection.setTransactionIsolation(level.getLevel());
		}
		setDesiredAutoCommit(autoCommmit);
	}

	@Override
	public Integer getTimeout() throws SQLException {
		return null;
	}

}
