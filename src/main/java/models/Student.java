package models;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private final String name;
    private final String ulearnID;
    private final String group;
    private final boolean isLocal;
    private final String city;
    private final int totalScore;
    private final List<Topic> topics;


    public Student(String name, String ulearnId, String group, boolean isLocal, int score, String city) {
        this.ulearnID = ulearnId;
        this.name = name;
        this.group = group;
        this.isLocal = isLocal;
        this.city = city;
        this.totalScore = score;
        this.topics = new ArrayList<>();
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    public String getName() {
        return name;
    }

    public String getUlearnId() {
        return ulearnID;
    }

    public String getGroup() {
        return group;
    }

    public List<Topic> getTopics() {
        return topics;
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
//        sb.append("Themes:\n");
//        for (Topic topic : topics) {
//            sb.append("\t").append(topic.toString()).append("\n");
//        }
        return sb.toString();
    }
}
