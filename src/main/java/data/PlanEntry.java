package data;

import java.util.ArrayList;
import java.util.List;

public class PlanEntry {
	private int suiteId;
	private String name;
	private String description;
	private Integer assignedtoId;
	private boolean includeAll;
	private List<Integer> configIds = new ArrayList<>();
	private List<Run> runs = new ArrayList<>();
	private List<Integer> caseIds = new ArrayList<>();

	public PlanEntry(int suiteId, String name, String description, int assignedToId, boolean includeAll,
			ArrayList<Integer> configIds, ArrayList<Run> runs) {
		super();
		this.suiteId = suiteId;
		this.name = name;
		this.description = description;
		this.assignedtoId = assignedToId;
		this.includeAll = includeAll;
		this.configIds = configIds;
		this.runs = runs;
	}

	public PlanEntry() {
		// TODO Auto-generated constructor stub
	}

	public int getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(int suiteId) {
		this.suiteId = suiteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAssignedToId() {
		return assignedtoId;
	}

	public void setAssignedToId(Integer assignedToId) {
		this.assignedtoId = assignedToId;
	}

	public boolean isIncludeAll() {
		return includeAll;
	}

	public void setIncludeAll(boolean includeAll) {
		this.includeAll = includeAll;
	}

	public List<Integer> getConfigIds() {
		return configIds;
	}

	public void setConfigIds(List<Integer> configIds) {
		this.configIds = configIds;
	}

	public List<Run> getRuns() {
		return runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}


	public List<Integer> getCaseIds() {
		return caseIds;
	}

	public void setCaseIds(List<Integer> caseIds) {
		this.caseIds = caseIds;
	}
}
