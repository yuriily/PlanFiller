package data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import models.*;

import java.util.*;

public class PlanEntry {
	private int suiteId;
	private String name;
	private String description;
	private Integer assignedtoId;
	private boolean includeAll;
	private List<Integer> configIds = new ArrayList<>();
	private List<Integer> caseIds = new ArrayList<>();
	private List<Run> runs = new ArrayList<>();

	@JsonIgnore
	private Set<Integer> includedConfigIds = new HashSet<>();
	@JsonIgnore
	private Set<Integer> uniqueRowEntityIds = new HashSet<>();


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

	public void addRunsFromRecordset(RailRecordSet railRecordSet, RailModel railModel, boolean isCasesInRows) {
		//todo check if we can separate test runs into different entries - group by column; applies only to config*config table

		//make a deep copy of all table rows
		List<RailRecord> localRecords = new ArrayList<>(railRecordSet.getRows().size());
		for(RailRecord railRecord:railRecordSet.getRows()) {
			try {
				//todo cannot use deepClone - it creates new instances of configurationItem objects so I cannot search for them whilke making
				//or change object comparison in search to .toString comparison, then first map key should be ConfigurationItem.toString
				localRecords.add((RailRecord)railRecord.clone());
			} catch (Exception e) { e.printStackTrace(); }
		}

		//iterate through all configurations;
		//test run can have only one configuration from configuration group
		List<Run> testRuns = new ArrayList<>();
		Set<Integer> includedConfigIds = new HashSet<>();
		Set<Integer> uniqueRowEntityIds = new HashSet<>();

		//FIRST CASE: case X config table
		if(isCasesInRows) {
			for (TestRailsEntity currentConfig : railRecordSet.getColumnNames()) {

				List<Integer> includedRowEntityIds = new ArrayList<>();
				//for each configuration iterate through the remaining list of table rows (either cases or other configurations)
				Iterator<RailRecord> recordIterator = localRecords.iterator();
				RailRecord record = null;

				while (recordIterator.hasNext()) {
					record = (RailRecord) recordIterator.next();

					//if case has a value for this configuration, add it
					if (record.getColumnValues().containsKey(currentConfig)) {
						includedRowEntityIds.add(record.getRowValue().getId());
						uniqueRowEntityIds.add(record.getRowValue().getId());
						//then delete this value from map
						record.getColumnValues().remove(currentConfig);
						//check if the map is empty, if yes - remove the case from the local copy of test cases
						if (record.getColumnValues().isEmpty())
							recordIterator.remove();

					}
				}

				//are there any rows that match current configuration?
				if (includedRowEntityIds.isEmpty())
					continue;

				Run run = new Run();
				run.setIncludeAll(false);
				run.setSuiteId(this.suiteId);
				run.setName("Configuration: " + currentConfig.getName());
				run.setDescription("Test run for configuration: " + currentConfig.getName());
				run.setConfigId(currentConfig.getId());
				run.setCaseIds((ArrayList<Integer>) includedRowEntityIds);
				includedConfigIds.add(currentConfig.getId());

				testRuns.add(run);
			}
			this.runs = testRuns;
			this.caseIds = new ArrayList<>(uniqueRowEntityIds);
			this.configIds = new ArrayList<>(includedConfigIds);
		} else {
			//SECOND CASE: config X config table
			for(TestRailsEntity currentconfigColumn: railRecordSet.getColumnNames()) {
				Iterator<RailRecord> recordIterator = localRecords.iterator();
				RailRecord record = null;
				boolean isUniqueColumnAdded = false;
				while(recordIterator.hasNext()) {
					record=(RailRecord) recordIterator.next();
					if(record.getColumnValues().containsKey(currentconfigColumn)) {
						uniqueRowEntityIds.add(record.getRowValue().getId());

						//only one insertion per column is made into set of unique config IDs
						if(!isUniqueColumnAdded) {
							uniqueRowEntityIds.add(currentconfigColumn.getId());
							isUniqueColumnAdded=true;
						}

						//create new run, it will have only one test and two configs, one from row and column
						Run run = new Run();
						run.setIncludeAll(false);
						run.setSuiteId(this.suiteId);
						run.setBothConfigIds(currentconfigColumn.getId(), record.getRowValue().getId());
						run.setCaseId(railModel.getSelectedCase());
						testRuns.add(run);

						//clean up the map and delete the record if it has no more values in the map
						record.getColumnValues().remove(currentconfigColumn);
						if(record.getColumnValues().isEmpty())
							recordIterator.remove();

					}
				}

			}
			this.runs=testRuns;
			this.caseIds = Collections.singletonList(railModel.getSelectedCase());
			this.configIds = new ArrayList<>(uniqueRowEntityIds);

		}


	}
}
