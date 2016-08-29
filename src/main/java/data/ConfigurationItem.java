package data;

public class ConfigurationItem extends TestRailsEntity {
	private int groupId;
	private int id;
	private String name;

	public ConfigurationItem() {}

	public ConfigurationItem(int groupId, int id, String name) {
		super();
		this.groupId = groupId;
		this.id = id;
		this.name = name;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
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
	
	

}
