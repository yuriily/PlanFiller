package data;

/**
 * Created by yuriily on 29-Aug-16.
 */
public class TestRailsEntity {
    private String name;
    private int id;

    public TestRailsEntity() {
        super();
    }



    @Override
    public String toString() {
        if(null== this.getName())
            this.setName("(empty)");
        String result = ((Integer) this.getId()).toString();
        //add up to length of 6 digits;
        // if there is more than 1 million records, please also change the number in models.CsvImporter.readFromCsv
        while(result.length()<6)
            result=" " + result;
        result+="| " + this.getName();
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {return false; }
        if (getClass() != object.getClass()) { return false; }
        if(getId() == ((TestRailsEntity)object).getId()) { return true; }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
