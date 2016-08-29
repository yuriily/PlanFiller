package data;

public class Configuration extends TestRailsEntity {
	private ConfigurationItem[] configs;
	private int id;
	private String name;
	private int projectId;

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
	
	

}
