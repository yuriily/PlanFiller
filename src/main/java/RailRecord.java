import data.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuriily on 05-Sep-16.
 */
public class RailRecord implements Cloneable {
    private TestRailsEntity rowValue;
    private Map<TestRailsEntity, String> columnValues = new HashMap<>();

    public TestRailsEntity getRowValue() {
        return rowValue;
    }

    public void setRowValue(TestRailsEntity rowValue) {
        this.rowValue = rowValue;
    }

    public Map<TestRailsEntity, String> getColumnValues() {
        return columnValues;
    }

    public void setColumnValues(Map<TestRailsEntity, String> columnValues) {
        this.columnValues = columnValues;
    }

    protected Object clone() throws CloneNotSupportedException {
        return (RailRecord)super.clone();
    }

}
