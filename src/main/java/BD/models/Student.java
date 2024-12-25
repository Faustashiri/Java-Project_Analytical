package BD.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "students")
public class Student {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String ulearnID;

    @DatabaseField
    private String studentGroup;

    @DatabaseField
    private boolean isLocal;

    @DatabaseField
    private String city;

    @DatabaseField
    private int totalScore;

    public Student() {}

    public Student(String name, String ulearnId, String studentGroup, boolean isLocal, int score, String city) {
        this.ulearnID = ulearnId;
        this.name = name;
        this.studentGroup = studentGroup;
        this.isLocal = isLocal;
        this.city = city;
        this.totalScore = score;
    }

    public String getName() {
        return name;
    }

    public String getUlearnId() {
        return ulearnID;
    }

    public String getGroup() {
        return studentGroup;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getCity() {
        return city;
    }

    public boolean getIsLocal() {
        return isLocal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student name: ").append(getName()).append("\n")
                .append("UlearnID: ").append(getUlearnId()).append("\n")
                .append("Group: ").append(getGroup()).append("\n")
                .append("Total score: ").append(getTotalScore()).append("\n")
                .append("IsLocal: ").append(getIsLocal()).append("\n")
                .append("City: ").append(getCity()).append("\n");
        return sb.toString();
    }
}