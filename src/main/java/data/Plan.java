package data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Plan extends TestRailsEntity {
    private int id;
    private int assignedtoId;
    private int blockedCount;
    private Long completedOn;
    private int createdBy;
    private Long createdOn;
    private int customStatus1Count;
    private int customStatus2Count;
    private int customStatus3Count;
    private int customStatus4Count;
    private int customStatus5Count;
    private int customStatus6Count;
    private int customStatus7Count;
    private int passedCount;
    @JsonProperty("is_completed")
    private boolean isCompleted;
	private String name;
	private String description;
	private int milestoneId;
	private ArrayList<PlanEntry> entries;

    public Plan() {}

	public Plan(String name, String description, int milestoneId, ArrayList<PlanEntry> entries) {
		super();
		this.name = name;
		this.description = description;
		this.milestoneId = milestoneId;
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
		return milestoneId;
	}
	public void setMilestoneId(int milestoneId) {
		this.milestoneId = milestoneId;
	}
	public ArrayList<PlanEntry> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<PlanEntry> entries) {
		this.entries = entries;
	}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getAssignedtoId() {
        return assignedtoId;
    }

    public void setAssignedtoId(int assignedtoId) {
        this.assignedtoId = assignedtoId;
    }

    public int getBlockedCount() {
        return blockedCount;
    }

    public void setBlockedCount(int blockedCount) {
        this.blockedCount = blockedCount;
    }

    public Long getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Long completedOn) {
        this.completedOn = completedOn;
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

    public int getCustomStatus1Count() {
        return customStatus1Count;
    }

    public void setCustomStatus1Count(int customStatus1Count) {
        this.customStatus1Count = customStatus1Count;
    }

    public int getCustomStatus2Count() {
        return customStatus2Count;
    }

    public void setCustomStatus2Count(int customStatus2Count) {
        this.customStatus2Count = customStatus2Count;
    }

    public int getCustomStatus3Count() {
        return customStatus3Count;
    }

    public void setCustomStatus3Count(int customStatus3Count) {
        this.customStatus3Count = customStatus3Count;
    }

    public int getCustomStatus4Count() {
        return customStatus4Count;
    }

    public void setCustomStatus4Count(int customStatus4Count) {
        this.customStatus4Count = customStatus4Count;
    }

    public int getCustomStatus5Count() {
        return customStatus5Count;
    }

    public void setCustomStatus5Count(int customStatus5Count) {
        this.customStatus5Count = customStatus5Count;
    }

    public int getCustomStatus6Count() {
        return customStatus6Count;
    }

    public void setCustomStatus6Count(int customStatus6Count) {
        this.customStatus6Count = customStatus6Count;
    }

    public int getCustomStatus7Count() {
        return customStatus7Count;
    }

    public void setCustomStatus7Count(int customStatus7Count) {
        this.customStatus7Count = customStatus7Count;
    }

    public int getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(int passedCount) {
        this.passedCount = passedCount;
    }
}
