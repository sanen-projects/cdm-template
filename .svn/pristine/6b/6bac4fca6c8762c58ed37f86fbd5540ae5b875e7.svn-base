package online.sanen.cdm.template;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mhdt.toolkit.Assert;

import online.sanen.cdm.template.transaction.JdbcTransaction;
import online.sanen.cdm.template.transaction.Transaction;

/**
 * 
 *
 * @author LazyToShow <br>
 *         Date: 2018年10月14日 <br>
 *         Time: 下午2:58:01
 */
public class DataSourceUtils {

	public static Connection getConnection(Transaction transaction, DataSource dataSource) throws SQLException {
		
		if (transaction != null)
			return transaction.getConnection();
		
		return fetchConnection(dataSource);
	}

	private static Connection fetchConnection(DataSource dataSource) throws SQLException {
		Connection con = dataSource.getConnection();
		if (con == null) {
			throw new IllegalStateException("DataSource returned null from getConnection(): " + dataSource);
		}
		return con;
	}

	public static void releaseConnection(Transaction transaction, Connection con) {

		if (transaction != null) {

			if (!(transaction instanceof JdbcTransaction))
				try {
					transaction.close();
				} catch (SQLException e) {
				//	logger.debug("Could not close JDBC Connection");
				}

		} else {
			try {
				con.close();
			} catch (SQLException ex) {
			//	logger.debug("Could not close JDBC Connection");
			} catch (Throwable ex) {
			//	logger.debug("Unexpected exception on closing JDBC Connection");
			}
		}

	}

	public static void applyTimeout(Statement stmt, DataSource dataSource, int queryTimeout) throws SQLException {

		Assert.notNull(stmt, "No Statement specified");

		if (queryTimeout >= 0) {
			// No current transaction timeout -> apply specified value.
			stmt.setQueryTimeout(queryTimeout);
		}

	}

}
