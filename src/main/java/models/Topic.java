package models;

public record Topic(String topicName, int exercisePoints, int homeworkPoints, int controlQuestionsPoints,
                    int maxExercisePoints, int maxHomeworkPoints, int maxControlQuestionsPoints) {

    @Override
    public String toString() {

        return "Theme: " + topicName +
                ", Exercise: " + exercisePoints + "/" + maxExercisePoints +
                ", Homework: " + homeworkPoints + "/" + maxHomeworkPoints +
                ", Quiz: " + controlQuestionsPoints + "/" + maxControlQuestionsPoints;
    }
}
