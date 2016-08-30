package data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Project extends TestRailsEntity {
	private int suiteMode;
	private Long completedOn;
	private boolean showAnnouncement;
    @JsonProperty("is_completed")
    private boolean isCompleted;
	private String url;
	private String announcement;

	@JsonIgnore
	private List<Plan> plans;
	@JsonIgnore
	private List<Suite> suites;
	@JsonIgnore
	private List<Configuration> configurations;

	//just to enable jackson serialization
	public Project() {}
	
	public Project(int suiteMode, Long completedOn, String name, int id, boolean showAnnouncement, boolean isCompleted,
			String url, String announcement) {
		super();
		
		//TODO: add check for not null for the fields: id, name, url
		
		this.suiteMode = suiteMode;
		this.completedOn = completedOn;
		super.setName(name);
		super.setId(id);
		this.showAnnouncement = showAnnouncement;
		this.isCompleted = isCompleted;
		this.url = url;
		this.announcement = announcement;
	}

	public int getSuiteMode() {
		return suiteMode;
	}

	public void setSuiteMode(int suiteMode) {
		this.suiteMode = suiteMode;
	}

	public Long getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(Long completedOn) {
		this.completedOn = completedOn;
	}

	public String getName() {
		return super.getName();
	}

	public void setName(String name) {
		super.setName(name);
	}

	public int getId() {
		return super.getId();
	}

	public void setId(int id) {
		super.setId(id);
	}

	public boolean isShowAnnouncement() {
		return showAnnouncement;
	}

	public void setShowAnnouncement(boolean showAnnouncement) {
		this.showAnnouncement = showAnnouncement;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}


	public List<Plan> getPlans() {
		return plans;
	}

	public void setPlans(List<Plan> plans) {
		this.plans = plans;
	}

	public List<Suite> getSuites() {
		return suites;
	}

	public void setSuites(List<Suite> suites) {
		this.suites = suites;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}
}
