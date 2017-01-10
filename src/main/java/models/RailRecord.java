package models;

import data.TestRailsEntity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuriily on 05-Sep-16.
 */
public class RailRecord implements Serializable, Cloneable {

    private static final long serialVersionUID = 6886831818174611072L;

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

    public RailRecord deepClone() {
        RailRecord oldRecord = this;
        RailRecord newRecord = new RailRecord();
        newRecord.rowValue = oldRecord.rowValue;
        Map<TestRailsEntity, String> newColumnValues = new HashMap<>();
        newColumnValues.putAll(oldRecord.columnValues);
        newRecord.columnValues = newColumnValues;
        /*
        try {
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(this);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
            newRecord = (RailRecord) in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        */
        return newRecord;
    }


}
