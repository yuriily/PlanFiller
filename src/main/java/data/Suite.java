package data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Suite {
	private Long completedOn;
	private String description;
	private int id;
	@JsonProperty("is_baseline")
	private boolean isBaseline;
	@JsonProperty("is_completed")
	private boolean isCompleted;
	@JsonProperty("is_master")
	private boolean isMaster;
	private String name;
	private int projectId;
	private String url;

	private Suite() {

	}
	
	public Suite(Long completedOn, String description, int id, boolean isBaseline, boolean isCompleted, boolean isMaster,
			String name, int projectId) {
		super();
		this.completedOn = completedOn;
		this.description = description;
		this.id = id;
		this.isBaseline = isBaseline;
		this.isCompleted = isCompleted;
		this.isMaster = isMaster;
		this.name = name;
		this.projectId = projectId;
	}

	public String toString() {
		String result = ((Integer)this.id).toString();
		while(result.length()<6)
			result=" " + result;
		result+="| " + this.name;
		return result;
	}


	public Long getCompletedOn() {
		return completedOn;
	}
	public String getDescription() {
		return description;
	}
	public int getId() {
		return id;
	}
	public boolean isBaseline() {
		return isBaseline;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public boolean isMaster() {
		return isMaster;
	}
	public String getName() {
		return name;
	}
	public int getProjectId() {
		return projectId;
	}
	public String getUrl() {
		return url;
	}
	
	

}
