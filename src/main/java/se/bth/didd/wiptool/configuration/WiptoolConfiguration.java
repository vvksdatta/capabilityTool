package se.bth.didd.wiptool.configuration;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import com.bazaarvoice.dropwizard.assets.AssetsBundleConfiguration;
import com.bazaarvoice.dropwizard.assets.AssetsConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
//import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/*
* Configuration class for dropwizard. Extend this class with additional
* configuration parameters.
*/
/*
 * The goal is to serve a single page app, while having DropWizard REST endpoints available at /api. 
 * The documentation around serving assets in DropWizard suggest that setting up an AssetsBundle 
 * Refer:
 * @see (https://spin.atomicobject.com/2014/10/11/serving-static-assets-with-dropwizard/ )
 * @see (https://github.com/bazaarvoice/dropwizard-configurable-assets-bundle )
*/
public class WiptoolConfiguration extends Configuration implements AssetsBundleConfiguration {
	@Valid
	@NotNull
	@JsonProperty
	private final AssetsConfiguration assets = new AssetsConfiguration();

	@Override
	public AssetsConfiguration getAssetsConfiguration() {
		return assets;
	}
	/*
	 * Adding a Managed instance to your application’s Environment ties that
	 * object’s life cycle to that of the application’s HTTP server. To create a
	 * managed, instrumented DBI instance, our configuration class needs a
	 * DataSourceFactory instance: Refer :
	 * https://github.com/stevenalexander/dropwizard-jdbi
	 * 
	 */

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	
	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory factory) {
		this.database = factory;
	}

	
	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}
	
	/*
	 * For importing redmine configuration settings. Refer @see (
	 * https://kielczewski.eu/2013/04/developing-restful-web-services-using-
	 * dropwizard/ for an example )
	 */

	@NotEmpty
	private String redmineUrl;
	
	@JsonProperty
	public String getRedmineUrl() {
		return redmineUrl;
	}

	@NotEmpty
	private String adminUserName;
	
	@NotEmpty
	private String adminPassword;

	public String getAdminUserName() {
		return adminUserName;
	}


	public String getAdminPassword() {
		return adminPassword;
	}


	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}


	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

}
