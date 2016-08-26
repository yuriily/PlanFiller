package data;

import java.util.ArrayList;

public class Plan {
	private String name;
	private String description;
	private int milestone_id;
	private ArrayList<PlanEntry> entries;

	public Plan(String name, String description, int milestoneId, ArrayList<PlanEntry> entries) {
		super();
		this.name = name;
		this.description = description;
		this.milestone_id = milestoneId;
		this.entries = entries;
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
	public int getMilestoneId() {
		return milestone_id;
	}
	public void setMilestoneId(int milestoneId) {
		this.milestone_id = milestoneId;
	}
	public ArrayList<PlanEntry> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<PlanEntry> entries) {
		this.entries = entries;
	}
	
	

}
