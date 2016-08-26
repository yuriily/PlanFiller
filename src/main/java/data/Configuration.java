package data;

public class Configuration {
	private ConfigurationItem configs[];
	private int id;
	private String name;
	private int project_id;
	public Configuration(ConfigurationItem[] configurationItems, int id, String name, int projectId) {
		super();
		this.configs = configurationItems;
		this.id = id;
		this.name = name;
		this.project_id = projectId;
	}
	public ConfigurationItem[] getConfigurationItems() {
		return configs;
	}
	public void setConfigurationItems(ConfigurationItem[] configurationItems) {
		this.configs = configurationItems;
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
		return project_id;
	}
	public void setProjectId(int projectId) {
		this.project_id = projectId;
	}
	
	

}
