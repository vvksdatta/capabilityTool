package se.bth.didd.wiptool.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.github.rkmk.mapper.FieldMapperFactory;

/*JDBI default mapper doesn't handle all the object types. (example sql array). If we want to have 
 * any special case while mapping fields, we can do that by implementing FieldMapperFactory.
 * @see https://github.com/Manikandan-K/jdbi-folder
 * This factory handles mapping of 'date' type.
*/
public class LocalDateMapperFactory implements FieldMapperFactory<LocalDate> {

	@Override
	public LocalDate getValue(ResultSet rs, int index, Class<?> type) throws SQLException {

		return rs.getDate(index).toLocalDate();
	}

	@Override
	public Boolean accepts(Class<?> type) {
		return type.isAssignableFrom(LocalDate.class);
	}

}
