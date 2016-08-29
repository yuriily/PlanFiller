package data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Suite extends TestRailsEntity {
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

	public Suite() {
	}
	
	public Suite(Long completedOn, String description, int id, boolean isBaseline, boolean isCompleted, boolean isMaster,
			String name, int projectId) {
		super();
		this.setCompletedOn(completedOn);
		this.setDescription(description);
		this.setId(id);
		this.setBaseline(isBaseline);
		this.setCompleted(isCompleted);
		this.setMaster(isMaster);
		this.setName(name);
		this.setProjectId(projectId);
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


	public void setCompletedOn(Long completedOn) {
		this.completedOn = completedOn;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public void setBaseline(boolean baseline) {
		isBaseline = baseline;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public void setMaster(boolean master) {
		isMaster = master;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
