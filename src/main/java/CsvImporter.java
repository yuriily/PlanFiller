import data.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yuriily on 07-Sep-16.
 */
public class CsvImporter {
    private RailRecordSet recordSet;
    private RailModel railModel;

    public CsvImporter(RailRecordSet recordSet) {
        this.recordSet = recordSet;
    }
    public CsvImporter(RailRecordSet recordSet, RailModel railModel) {
        this.recordSet = recordSet; this.railModel = railModel;
    }

    public void writeToCSV(String fileName) throws IOException {
        ICsvMapWriter mapWriter = null;

        //create header for the table
        final String[] header = new String[recordSet.getColumnNames().size()+1];
        //first column contains info about row and column entity type
        header[0] = ((TestRailsEntity)recordSet.getRows().get(0).getRowValue()).getClass().getSimpleName() + " \\ " +
                recordSet.getColumnNames().get(0).getClass().getSimpleName();
        Iterator<TestRailsEntity> iterCols = recordSet.getColumnNames().iterator();
        //other columns
        int iter=1;
        while(iterCols.hasNext())
            header[iter++] = iterCols.next().toString();

        final CellProcessor[] processors = new CellProcessor[header.length];

        try {
            mapWriter = new CsvMapWriter(new FileWriter(fileName), CsvPreference.STANDARD_PREFERENCE);
            mapWriter.writeHeader(header);
            //write other values by processing all table rows
            //csv writer doesn't understand object as a key, so translate it to string
            for(RailRecord currentRecord : recordSet.getRows()) {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put(header[0], currentRecord.getRowValue().toString());
                for(Map.Entry<TestRailsEntity, String> entry : currentRecord.getColumnValues().entrySet()) {
                    stringMap.put(entry.getKey().toString(),entry.getValue());
                }
                mapWriter.write(stringMap,header,processors);
            }


        } finally {
            if(mapWriter!=null)
                mapWriter.close();
        }

    }

    public void readFromCSV(String fileName) throws IOException {
        ICsvMapReader mapReader=null;
        RailClient client=RailClient.getInstance();

        try {
            mapReader = new CsvMapReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
            // read the header to know the number of cell processors
            // and set our columns set
            // also the header has a right sequence of configurations, while mapReader.read order is random
            final String[] header = mapReader.getHeader(true);
            if(header==null) {
                throw new IOException("File" +fileName + "is empty.");
            }
            //in the first column, first row there is an info about class of test entities
            //looks like "Case \ Configuration", \ is delimiter
            if(!header[0].contains("\\"))
                throw new IOException("File " + fileName + "has corrupted header.");

            String[] entities = header[0].split(" \\\\ ");
            final CellProcessor[] processors = new CellProcessor[header.length];
            Map<String, Object> oneRowRecord;
            oneRowRecord = mapReader.read(header,processors);

            //put items from current project, from ALL configurations into one map - it will be easier to work with them
            Map<Integer, ConfigurationItem> allConfigItems=new HashMap<>();
            for(Configuration config : railModel.getCurrentConfigurations()) {
                for(ConfigurationItem configItem: config.getConfigurationItems()) {
                    allConfigItems.put(configItem.getId(), configItem);
                }
            }

            if(entities[0].equals("Case") && entities[1].equals("ConfigurationItem")) {
                //we've got cases in the rows, configurations in the columns

                //read first record and check if this test suite belongs to current project
                int firstCaseId = Integer.parseInt(oneRowRecord.get(header[0]).toString().substring(0,6).trim());

                try {
                    Case firstCase = client.getOneInstance(firstCaseId, Case.class);
                    Suite suiteForFirstCase = client.getOneInstance(firstCase.getSuiteId(), Suite.class);
                    if(!railModel.getCurrentSuites().contains(suiteForFirstCase))
                        throw new IOException("Cannot identify test suite for case number: "+firstCaseId+". Maybe it was moved to another suite");
                } catch (Exception e) {
                    System.out.println("An error during retrieving test case number: "+firstCaseId);
                    e.printStackTrace();
                }

                //now we've got a suite and can search for cases inside it

                //create a list of column names for record set
                for(int iter=1; iter<header.length;iter++) {
                    int configItemId = Integer.parseInt(header[iter].substring(0,6).trim());
                    recordSet.addColumnName(allConfigItems.get(configItemId));
                }

                //now read the file record by record and add corresponding values to the record
                while ((oneRowRecord = mapReader.read(header, processors)) != null) {
                    RailRecord railRecord = new RailRecord();
                    int rowRecordId = Integer.parseInt(oneRowRecord.get(header[0]).toString().substring(0,6).trim());
                    try {
                        railRecord.setRowValue(client.getOneInstance(rowRecordId, Case.class));
                        //leave only the values that should be mapped, without row name
                        oneRowRecord.remove(header[0]);
                        for(Map.Entry<String,Object> entry : oneRowRecord.entrySet()) {
                            //values should be replaced by strings, and not placed at all if they are null
                            if(entry.getValue()!=null) {
                                //keys should be replaced by configuration items
                                ConfigurationItem configItem = allConfigItems.get(Integer.parseInt(entry.getKey().substring(0,6).trim()));
                                railRecord.getColumnValues().put(configItem, entry.getValue().toString());
                            }
                        }
                        recordSet.addRow(railRecord);
                    } catch (Exception e) {
                        System.out.println("An error during retrieving test case number: "+firstCaseId);
                        e.printStackTrace();
                    }
                }

            }
            if(entities[0].equals("ConfigurationItem") && entities[1].equals("ConfigurationItem")) {
                //we've got config X config table
                //it is too expensive to check if all configuration items are available now
                //we will skip some test creation if we won't find some

                //create a list of column names for record set
                for(int iter=1; iter<header.length;iter++) {
                    int configItemId = Integer.parseInt(header[iter].substring(0,6).trim());
                    recordSet.addColumnName(allConfigItems.get(configItemId));
                }

                //now read the file record by record and add corresponding values to the record
                while ((oneRowRecord = mapReader.read(header, processors)) != null) {
                    RailRecord railRecord = new RailRecord();
                    int rowRecordId = Integer.parseInt(oneRowRecord.get(header[0]).toString().substring(0,6).trim());
                    try {
                        //get a configItem from the merged list of both configs, that we've created earlier
                        railRecord.setRowValue(allConfigItems.get(rowRecordId));
                        //leave only the values that should be mapped, without row name
                        oneRowRecord.remove(header[0]);
                        for(Map.Entry<String,Object> entry : oneRowRecord.entrySet()) {
                            //values should be replaced by strings, and not placed at all if they are null
                            if(entry.getValue()!=null) {
                                //keys should be replaced by configuration items
                                ConfigurationItem configItem = allConfigItems.get(Integer.parseInt(entry.getKey().substring(0,6).trim()));
                                railRecord.getColumnValues().put(configItem, entry.getValue().toString());
                            }
                        }
                        recordSet.addRow(railRecord);
                    } catch (Exception e) {
                        System.out.println("An error during retrieving configuration with parameter id: " + rowRecordId);
                        e.printStackTrace();
                    }
                }



            }

        } finally {
            if(mapReader!=null)
                mapReader.close();
        }

    }

    public RailRecordSet getRecordSet() {
        return recordSet;
    }

    public void setRecordSet(RailRecordSet recordSet) {
        this.recordSet = recordSet;
    }
}
