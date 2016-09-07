import data.TestRailsEntity;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

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

    public CsvImporter(RailRecordSet recordSet) {
        this.recordSet = recordSet;
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

    }

    public RailRecordSet getRecordSet() {
        return recordSet;
    }

    public void setRecordSet(RailRecordSet recordSet) {
        this.recordSet = recordSet;
    }
}
