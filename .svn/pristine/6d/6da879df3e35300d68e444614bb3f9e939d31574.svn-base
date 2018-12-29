package online.sanen.cdm.template;

import java.sql.SQLException;

import javax.sql.DataSource;

import online.sanen.cdm.template.transaction.Transaction;
import online.sanen.cdm.template.transaction.TransactionFactory;
import online.sanen.cdm.template.transaction.TransactionIsolationLevel;

/**
 * 
 *
 * @author LazyToShow <br>
 *         Date: Nov 5, 2018 <br>
 *         Time: 6:26:31 PM
 */
public class TransactionManager {

	private static ThreadLocal<Transaction> threadLcoal = new ThreadLocal<>();
	
	private static TransactionFactory factory;

	public static Transaction getTransaction(DataSource dataSource,TransactionIsolationLevel level,boolean flag) {

		Transaction transaction = threadLcoal.get();
		
		if(transaction == null && factory!=null) {
			transaction = factory.newTransaction(dataSource, level, flag);
			threadLcoal.set(transaction);
		}
		
		return transaction;
	}
	
	
	public static void bindTransactionFatory(TransactionFactory factory) {
		TransactionManager.factory = factory;
	}

	public static void closeSqlTransaction() throws SQLException {

		Transaction transaction = threadLcoal.get();
		if (transaction != null) {
			transaction.close();
			threadLcoal.remove();
		}
		
		factory = null;
	}

}
