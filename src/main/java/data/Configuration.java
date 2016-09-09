package data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Configuration extends TestRailsEntity {
	private ConfigurationItem[] configs;
	private int id;
	private String name;
	private int projectId;

	@JsonIgnore
	private List<ConfigurationItem> configurationItems;

	public Configuration() {}

	public Configuration(ConfigurationItem[] configs, int id, String name, int projectId) {
		super();
		this.configs = configs;
		this.id = id;
		this.name = name;
		this.projectId = projectId;
	}
	public ConfigurationItem[] getConfigs() {
		return configs;
	}
	public void setConfigs(ConfigurationItem[] configs) {
		this.configs = configs;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	//if there are no configuration items, let's populate them from existing array
	public List<ConfigurationItem> getConfigurationItems() {
		if(configurationItems==null && configs!=null)
			configurationItems = Arrays.asList(configs);

		return configurationItems;
	}

	public void setConfigurationItems(List<ConfigurationItem> configurationItems) {
		this.configurationItems = configurationItems;
	}
}
