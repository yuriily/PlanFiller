package data;

import java.util.ArrayList;

public class Run {
	private Integer assignedtoId;
	private Integer blockedCount;
	private ArrayList<Integer> caseIds;
	private Long completedOn;
	private String config;
	private ArrayList<Integer> configIds;
	private Integer createdBy;
	private Long createdOn;
	private Integer customStatus1Count;
	private Integer customStatus2Count;
	private Integer customStatus3Count;
	private Integer customStatus4Count;
	private Integer customStatus5Count;
	private Integer customStatus6Count;
	private Integer customStatus7Count;
	private String description;
	private Integer failedCount;
	private Integer id;
	private boolean includeAll;
	private boolean isCompleted;
	private Integer milestoneId;
	private Integer planId;
	private String name;
	private Integer passedCount;
	private Integer projectId;
	private Integer resetCount;
	private Integer suiteId;
	private Integer untestedCount;
	private String url;
	
	public Run(int assignedTo, int blockedCount, Long completedOn, String config, ArrayList<Integer> configIds, int createdBy,
			Long createdOn, int customStatus1Count, int customStatus2Count, int customStatus3Count,
			int customStatus4Count, int customStatus5Count, int customStatus6Count, int customStatus7Count,
			String description, int failedCount, int id, boolean includeAll, boolean isCompleted, int milestoneId,
			int planId, String name, int passedCount, int projectId, int resetCount, int suiteId, int untestedCount,
			String url) {
		super();
		this.assignedtoId = assignedTo;
		this.blockedCount = blockedCount;
		this.completedOn = completedOn;
		this.config = config;
		this.configIds = configIds;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.customStatus1Count = customStatus1Count;
		this.customStatus2Count = customStatus2Count;
		this.customStatus3Count = customStatus3Count;
		this.customStatus4Count = customStatus4Count;
		this.customStatus5Count = customStatus5Count;
		this.customStatus6Count = customStatus6Count;
		this.customStatus7Count = customStatus7Count;
		this.description = description;
		this.failedCount = failedCount;
		this.id = id;
		this.includeAll = includeAll;
		this.isCompleted = isCompleted;
		this.milestoneId = milestoneId;
		this.planId = planId;
		this.name = name;
		this.passedCount = passedCount;
		this.projectId = projectId;
		this.resetCount = resetCount;
		this.suiteId = suiteId;
		this.untestedCount = untestedCount;
		this.url = url;
	}
	public Run() {
		// TODO Auto-generated constructor stub
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public Long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isIncludeAll() {
		return includeAll;
	}
	public void setIncludeAll(boolean includeAll) {
		this.includeAll = includeAll;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ArrayList<Integer> getCaseIds() {
		return caseIds;
	}

	public void setCaseIds(ArrayList<Integer> caseIds) {
		this.caseIds = caseIds;
	}
	public void setCaseId(int caseIds) {
		ArrayList<Integer> caseIdsList = new ArrayList<>();
		caseIdsList.add(caseIds);
		this.caseIds = caseIdsList;
	}

	public ArrayList<Integer> getConfigIds() {
		return configIds;
	}
	public void setConfigIds(ArrayList<Integer> configIds) {
		this.configIds = configIds;
	}
	public void setConfigId(int configIds) {
		ArrayList<Integer> configIdsList = new ArrayList<>();
		configIdsList.add(configIds);
		this.configIds = configIdsList;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPlanId() {
		return planId;
	}
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getSuiteId() {
		return suiteId;
	}
	public void setSuiteId(Integer suiteId) {
		this.suiteId = suiteId;
	}
	public void setMilestoneId(Integer milestoneId) {
		this.milestoneId = milestoneId;
	}
	public Integer getAssignedToId() {
		return assignedtoId;
	}
	public void setAssignedToId(Integer assignedToId) {
		this.assignedtoId = assignedToId;
	}
	public Integer getBlockedCount() {
		return blockedCount;
	}
	public void setBlockedCount(Integer blockedCount) {
		this.blockedCount = blockedCount;
	}
	public Long getCompletedOn() {
		return completedOn;
	}
	public void setCompletedOn(Long completedOn) {
		this.completedOn = completedOn;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getCustomStatus1Count() {
		return customStatus1Count;
	}
	public void setCustomStatus1Count(Integer customStatus1Count) {
		this.customStatus1Count = customStatus1Count;
	}
	public Integer getCustomStatus2Count() {
		return customStatus2Count;
	}
	public void setCustomStatus2Count(Integer customStatus2Count) {
		this.customStatus2Count = customStatus2Count;
	}
	public Integer getCustomStatus3Count() {
		return customStatus3Count;
	}
	public void setCustomStatus3Count(Integer customStatus3Count) {
		this.customStatus3Count = customStatus3Count;
	}
	public Integer getCustomStatus4Count() {
		return customStatus4Count;
	}
	public void setCustomStatus4Count(Integer customStatus4Count) {
		this.customStatus4Count = customStatus4Count;
	}
	public Integer getCustomStatus5Count() {
		return customStatus5Count;
	}
	public void setCustomStatus5Count(Integer customStatus5Count) {
		this.customStatus5Count = customStatus5Count;
	}
	public Integer getCustomStatus6Count() {
		return customStatus6Count;
	}
	public void setCustomStatus6Count(Integer customStatus6Count) {
		this.customStatus6Count = customStatus6Count;
	}
	public Integer getCustomStatus7Count() {
		return customStatus7Count;
	}
	public void setCustomStatus7Count(Integer customStatus7Count) {
		this.customStatus7Count = customStatus7Count;
	}
	public Integer getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}
	public Integer getPassedCount() {
		return passedCount;
	}
	public void setPassedCount(Integer passedCount) {
		this.passedCount = passedCount;
	}
	public Integer getResetCount() {
		return resetCount;
	}
	public void setResetCount(Integer resetCount) {
		this.resetCount = resetCount;
	}
	public Integer getUntestedCount() {
		return untestedCount;
	}
	public void setUntestedCount(Integer untestedCount) {
		this.untestedCount = untestedCount;
	}
	public Integer getMilestoneId() {
		return milestoneId;
	}
	
	
	
}
