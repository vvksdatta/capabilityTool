package se.bth.didd.wiptool.db;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import se.bth.didd.wiptool.api.OptionsTemplate;

/*A simple DAO interface to handle the database operations required for authentication */

public interface OptionsDAO {

	@SqlUpdate("create table if not exists OPTIONS (optionId serial primary key, addNewProject int, addNewPerson int, addNewSprint int)")
	void createOptionsTable();

	@SqlUpdate("insert into OPTIONS (addNewProject, addNewPerson, addNewSprint) values (:addNewProject, :addNewPerson, :addNewSprint)")
	int insertIntoOptionsTable(@BindBean OptionsTemplate options);

	@SqlQuery("select exists (select 1 from OPTIONS where addNewProject != '0' )")
	boolean ifAddNewProjectSettingEnabled();
	
	@SqlQuery("select exists (select 1 from OPTIONS where addNewPerson != '0' )")
	boolean ifAddNewPersonSettingEnabled();

	@SqlQuery("select exists (select 1 from OPTIONS where addNewSprint != '0' )")
	boolean ifAddNewSprintSettingEnabled();

	@SqlQuery("select exists (select 1 from OPTIONS where addNewProject != '1' )")
	boolean ifAddNewProjectSettingDisabled();
	
	@SqlQuery("select exists (select 1 from OPTIONS where addNewPerson != '1' )")
	boolean ifAddNewPersonSettingDisabled();

	@SqlQuery("select exists (select 1 from OPTIONS where addNewSprint != '1' )")
	boolean ifAddNewSprintSettingDisabled();
	
	@SqlQuery("select exists (select 1 from OPTIONS where optionId = '1' )")
	boolean ifOptionsAdded();
	
	@SqlUpdate("Update OPTIONS set  addNewProject = :addNewProject, addNewPerson = :addNewPerson, addNewSprint = :addNewSprint  where optionId = '1'")
	void updateAllOptions(@BindBean OptionsTemplate OptionsTemplate);
	
	@SqlUpdate("Update OPTIONS set  addNewProject = :addNewProject where optionId = '1'")
	void updateAddNewProjectOption(@BindBean OptionsTemplate OptionsTemplate);

	@SqlUpdate("Update OPTIONS set  addNewPerson = :addNewPerson  where optionId = '1'")
	void updateAddNewPersonOption(@BindBean OptionsTemplate OptionsTemplate);

	@SqlUpdate("Update OPTIONS set  addNewSprint = :addNewSprint  where optionId = '1'")
	void updateAddNewSprintOption(@BindBean OptionsTemplate OptionsTemplate);
	
	@SqlQuery("select addNewProject from OPTIONS where optionId = '1'")
	List<Integer> selectAddNewProjectSetting();
	
	@SqlQuery("select addNewPerson from OPTIONS where optionId = '1'")
	List<Integer> selectAddNewPersonSetting();

	@SqlQuery("select addNewSprint from OPTIONS where optionId = '1'")
	List<Integer> selectAddNewSprintSetting();
	
	@SqlQuery("select * from OPTIONS where optionId = '1'")
	List<OptionsTemplate> selectSettingsOfAllOptions();

}
