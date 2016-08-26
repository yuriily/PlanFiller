package data;

public class ConfigurationItem {
	private int group_id;
	private int id;
	private String name;
	public ConfigurationItem(int groupId, int id, String name) {
		super();
		this.group_id = groupId;
		this.id = id;
		this.name = name;
	}
	public int getGroupId() {
		return group_id;
	}
	public void setGroupId(int groupId) {
		this.group_id = groupId;
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
