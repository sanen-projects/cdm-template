package online.sanen.cdm.template;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.mhdt.Print;
import com.mhdt.toolkit.Reflect;

/**
 * 
 *
 * @author LazyToShow <br>
 *         Date: 2018年10月15日 <br>
 *         Time: 下午5:10:52
 */
public class RowExtractor2Entry<T> implements RowExtractor<T> {

	Class<T> cls;

	private Map<Integer, String> columnLabelMap;

	public RowExtractor2Entry(Class<T> cls) {
		this.cls = cls;
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		if (columnLabelMap == null)
			initColumnLabelMap(rs);

		return parse(rs);
	}


	public T parse(ResultSet rs) throws SQLException {

		T t = null;
		try {
			t = (T) cls.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e1) {
			Print.error("Init faild: because bean has not constructor default");
			e1.printStackTrace();
			return null;
		}

		for (int i = 1; i <= columnLabelMap.size(); i++) {
			String key = columnLabelMap.get(i);
			Object resultSetValue = JdbcUtils.getResultSetValue(rs, i);
			Reflect.setInject(t, key, resultSetValue);
		}

		return t;
	}

	private void initColumnLabelMap(ResultSet resultSet) {

		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();

			if (rsmd != null) {

				int columnCount = rsmd.getColumnCount();
				this.columnLabelMap = new HashMap<Integer, String>(columnCount);

				for (int i = 1; i <= columnCount; i++) {
					String columnName = JdbcUtils.lookupColumnName(rsmd, i);
					
					// Make sure to preserve first matching column for any given name,
					// as defined in ResultSet's type-level javadoc (lines 81 to 83).
					if (!this.columnLabelMap.containsValue(columnName)) {
						this.columnLabelMap.put(i, columnName);
					}
				}

			} else {
				this.columnLabelMap = Collections.emptyMap();
			}

		} catch (SQLException se) {
			throw new InvalidResultSetAccessException(se);
		}

	}

}
