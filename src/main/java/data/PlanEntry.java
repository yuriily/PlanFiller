package data;

import java.util.ArrayList;

public class PlanEntry {
	private int suite_id;
	private String name;
	private String description;
	private Integer assignedto_id;
	private boolean include_all;
	private ArrayList<Integer> config_ids;
	private ArrayList<Run> runs;
	
	public PlanEntry(int suiteId, String name, String description, int assignedToId, boolean includeAll,
			ArrayList<Integer> configIds, ArrayList<Run> runs) {
		super();
		this.suite_id = suiteId;
		this.name = name;
		this.description = description;
		this.assignedto_id = assignedToId;
		this.include_all = includeAll;
		this.config_ids = configIds;
		this.runs = runs;
	}

	public PlanEntry() {
		// TODO Auto-generated constructor stub
	}

	public int getSuiteId() {
		return suite_id;
	}

	public void setSuiteId(int suiteId) {
		this.suite_id = suiteId;
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
		return assignedto_id;
	}

	public void setAssignedToId(Integer assignedToId) {
		this.assignedto_id = assignedToId;
	}

	public boolean isIncludeAll() {
		return include_all;
	}

	public void setIncludeAll(boolean includeAll) {
		this.include_all = includeAll;
	}

	public ArrayList<Integer> getConfigIds() {
		return config_ids;
	}

	public void setConfigIds(ArrayList<Integer> configIds) {
		this.config_ids = configIds;
	}

	public ArrayList<Run> getRuns() {
		return runs;
	}

	public void setRuns(ArrayList<Run> runs) {
		this.runs = runs;
	}
	
	

}
