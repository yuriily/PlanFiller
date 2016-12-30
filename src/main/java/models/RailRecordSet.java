package models; /**
 * Created by yuriily on 05-Sep-16.
 */

import data.TestRailsEntity;

import java.util.ArrayList;
import java.util.List;

public class RailRecordSet {
    private List<RailRecord> rows;
    private List<TestRailsEntity> columnNames;

    public List<RailRecord> getRows() {
        return rows;
    }

    public void setRows(List<RailRecord> rows) {
        this.rows = rows;
    }

    public void addRow(RailRecord record) {
        if(record==null)
            return;
        if(this.rows==null)
            this.rows = new ArrayList<>();
        this.rows.add(record);
    }

    public void addColumnName(TestRailsEntity entity) {
        if(entity==null)
            return;
        if(this.columnNames==null)
            this.columnNames = new ArrayList<>();
        this.columnNames.add(entity);
    }

    //searches for models.RailRecord by its row
    public RailRecord getRecordFromRow(TestRailsEntity railsEntity) {
        for(RailRecord railRecord : rows) {
            if(railRecord.getRowValue().equals(railsEntity))
                return railRecord;
        }
        return null;
    }

    //counts the number of entries - e.g. table cells with nonempty values
    public int countEntries() {
        int result = 0;
        for(RailRecord record : this.rows)
            result += record.getColumnValues().size();
        return result;

    }

    public List<TestRailsEntity> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<TestRailsEntity> columnNames) {
        this.columnNames = columnNames;
    }

}
