package data;

public class Case {
	private int created_by;
	private Long created_on;
	private String estimate;
	private String estimate_forecast;
	private int id;
	private int milestone_id;
	private int priority_id;
	private String refs;
	private int section_id;
	private int suite_id;
	private int template_id;
	private String title;
	private String type_id;
	private int updated_by;
	private Long updated_on;
	public Case(int createdBy, Long createdOn, String estimate, String estimateForecast, int id, int milestoneId,
			int priorityId, String refs, int sectionId, int suiteId, int templateId, String title, String typeId,
			int updatedBy, Long updatedOn) {
		super();
		this.created_by = createdBy;
		this.created_on = createdOn;
		this.estimate = estimate;
		this.estimate_forecast = estimateForecast;
		this.id = id;
		this.milestone_id = milestoneId;
		this.priority_id = priorityId;
		this.refs = refs;
		this.section_id = sectionId;
		this.suite_id = suiteId;
		this.template_id = templateId;
		this.title = title;
		this.type_id = typeId;
		this.updated_by = updatedBy;
		this.updated_on = updatedOn;
	}
	public int getCreatedBy() {
		return created_by;
	}
	public void setCreatedBy(int createdBy) {
		this.created_by = createdBy;
	}
	public Long getCreatedOn() {
		return created_on;
	}
	public void setCreatedOn(Long createdOn) {
		this.created_on = createdOn;
	}
	public String getEstimate() {
		return estimate;
	}
	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}
	public String getEstimateForecast() {
		return estimate_forecast;
	}
	public void setEstimateForecast(String estimateForecast) {
		this.estimate_forecast = estimateForecast;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMilestoneId() {
		return milestone_id;
	}
	public void setMilestoneId(int milestoneId) {
		this.milestone_id = milestoneId;
	}
	public int getPriorityId() {
		return priority_id;
	}
	public void setPriorityId(int priorityId) {
		this.priority_id = priorityId;
	}
	public String getRefs() {
		return refs;
	}
	public void setRefs(String refs) {
		this.refs = refs;
	}
	public int getSectionId() {
		return section_id;
	}
	public void setSectionId(int sectionId) {
		this.section_id = sectionId;
	}
	public int getSuiteId() {
		return suite_id;
	}
	public void setSuiteId(int suiteId) {
		this.suite_id = suiteId;
	}
	public int getTemplateId() {
		return template_id;
	}
	public void setTemplateId(int templateId) {
		this.template_id = templateId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTypeId() {
		return type_id;
	}
	public void setTypeId(String typeId) {
		this.type_id = typeId;
	}
	public int getUpdatedBy() {
		return updated_by;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updated_by = updatedBy;
	}
	public Long getUpdatedOn() {
		return updated_on;
	}
	public void setUpdatedOn(Long updatedOn) {
		this.updated_on = updatedOn;
	}
	
	
	

}
