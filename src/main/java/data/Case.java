package data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Case extends TestRailsEntity {
	private int createdBy;
	private Long createdOn;
	private String estimate;
	private String estimateForecast;
	private int id;
	private int milestoneId;
	private int priorityId;
	private String refs;
	private int sectionId;
	private int suiteId;
	private int templateId;
	private String title;
	private String typeId;
	private int updatedBy;
	private Long updatedOn;

	public Case() {

	}

	public Case(int createdBy, Long createdOn, String estimate, String estimateForecast, int id, int milestoneId,
			int priorityId, String refs, int sectionId, int suiteId, int templateId, String title, String typeId,
			int updatedBy, Long updatedOn) {
		super();
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.estimate = estimate;
		this.estimateForecast = estimateForecast;
		this.id = id;
		this.milestoneId = milestoneId;
		this.priorityId = priorityId;
		this.refs = refs;
		this.sectionId = sectionId;
		this.suiteId = suiteId;
		this.templateId = templateId;
		this.title = title;
		this.typeId = typeId;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	@Override
	public String toString() {
		if(null== this.getTitle())
			this.setName("(empty)");
		String result = ((Integer) this.getId()).toString();
		while(result.length()<6)
			result=" " + result;
		result+="| " + this.getTitle();
		return result;
	}

	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	public String getEstimate() {
		return estimate;
	}
	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}
	public String getEstimateForecast() {
		return estimateForecast;
	}
	public void setEstimateForecast(String estimateForecast) {
		this.estimateForecast = estimateForecast;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMilestoneId() {
		return milestoneId;
	}
	public void setMilestoneId(int milestoneId) {
		this.milestoneId = milestoneId;
	}
	public int getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(int priorityId) {
		this.priorityId = priorityId;
	}
	public String getRefs() {
		return refs;
	}
	public void setRefs(String refs) {
		this.refs = refs;
	}
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public int getSuiteId() {
		return suiteId;
	}
	public void setSuiteId(int suiteId) {
		this.suiteId = suiteId;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Long updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	
	

}
