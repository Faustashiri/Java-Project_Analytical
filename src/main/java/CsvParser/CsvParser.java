package CsvParser;

import BD.BDRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import models.Student;
import models.Topic;
import vkAPI.VkApiSearch;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.*;

import static vkAPI.VkApiSearch.unknownCityCount;

public class CsvParser {
    public static String homeworkTask;
    public static String quizTask;
    public static String exerciseTask;

    public static List<Student> CSVToStudents(String filePath) throws IOException, CsvException {
        List<Student> students = new ArrayList<>();
        List<String> topicNames = new ArrayList<>();
        List<Integer> topicIndex = new ArrayList<>();
        Map<String, List<Integer>> topicTask = new HashMap<>();
        Map<Integer, Integer> taskMaxScore = new HashMap<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] topicsRow = reader.readNext();
            String[] tasksRow = reader.readNext();
            String[] maxScoresRow = reader.readNext();

            exerciseTask = tasksRow[3];
            homeworkTask = tasksRow[4];
            quizTask = tasksRow[5];

            List<Integer> currentTask = new ArrayList<>();
            for (int i = 1; i < topicsRow.length; i++) {
                String topic = topicsRow[i].trim();
                if (topic.contains(".")) {
                    if (!topicNames.isEmpty()) {
                        String preTopic = topicNames.getLast();
                        topicTask.put(preTopic, new ArrayList<>(currentTask));
                        currentTask.clear();
                    }
                    topicNames.add(topic);
                    topicIndex.add(i);
                }
                if (i >= 6) {
                    String task = tasksRow[i].trim();
                    if (exerciseTask.equals(task) || homeworkTask.equals(task) || quizTask.equals(task)) {
                        currentTask.add(i);
                        if (!maxScoresRow[i].trim().isEmpty() && maxScoresRow[i].matches("\\d+")) {
                            taskMaxScore.put(i, Integer.parseInt(maxScoresRow[i].trim()));
                        } else {
                            taskMaxScore.put(i, 0);
                        }
                    }
                }
            }

            if (!currentTask.isEmpty() && !topicNames.isEmpty()) {
                String lastTopic = topicNames.getLast();
                topicTask.put(lastTopic, new ArrayList<>(currentTask));
            }

            String[] line;

            while ((line = reader.readNext()) != null) {
                students.add(createStudent(line, topicNames, tasksRow, taskMaxScore, topicTask));
            }
        }

        return students;
    }

    private static Student createStudent(String[] studentInfo, List<String> topicNames, String[] tasks,
                                         Map<Integer, Integer> taskMaxScore,
                                         Map<String, List<Integer>> topicTask) {

        String name = studentInfo[0];
        String ulearnID = studentInfo[1];
        String group = studentInfo[2];
        String city = VkApiSearch.getCityFromStudent(name);
        boolean isLocal = city.equalsIgnoreCase("Екатеринбург");


        int totalScore = Integer.parseInt(studentInfo[3]) +
                Integer.parseInt(studentInfo[4]) +
                Integer.parseInt(studentInfo[5]);

        Student student = new Student(name, ulearnID, group, isLocal, totalScore, city);



        for (String topicName : topicNames) {
            int[] scores = new int[3];
            List<Integer> taskIndexes = topicTask.get(topicName);

            if (taskIndexes != null) {
                for (Integer index : taskIndexes) {
                    String taskType = tasks[index].trim();
                    int points = 0;
                    String pointsStr = studentInfo[index];
                    if (!pointsStr.isEmpty() && pointsStr.matches("\\d+")) {
                        points = Integer.parseInt(pointsStr);
                    }

                    if (exerciseTask.equals(taskType)) {
                        scores[0] += points;
                    } else if (homeworkTask.equals(taskType)) {
                        scores[1] += points;
                    } else if (quizTask.equals(taskType)) {
                        scores[2] += points;
                    }
                }
            }

            int maxExercisePoints = 0;
            int maxHomeworkPoints = 0;
            int maxControlQuestionsPoints = 0;

            if (taskIndexes != null) {
                for (Integer taskIndex : taskIndexes) {
                    String taskType = tasks[taskIndex].trim();
                    int maxScore = taskMaxScore.getOrDefault(taskIndex, 0);

                    if (exerciseTask.equals(taskType)) {
                        maxExercisePoints = maxScore;
                    } else if (homeworkTask.equals(taskType)) {
                        maxHomeworkPoints = maxScore;
                    } else if (quizTask.equals(taskType)) {
                        maxControlQuestionsPoints = maxScore;
                    }
                }
            }

            student.addTopic(new Topic(topicName, scores[0], scores[1], scores[2],
                    maxExercisePoints, maxHomeworkPoints, maxControlQuestionsPoints));
        }

        return student;
    }

    public static void main(String[] args) {
        String filePath = "java-rtf.csv";
        try {
            System.setOut(new PrintStream(System.out, true, Charset.defaultCharset()));

            BDRepository.connect();

            BDRepository.createStudentsTable();

            List<Student> students = CSVToStudents(filePath);

            // Подсчет количества студентов
            int totalStudents = students.size();

            // Подсчет количества местных студентов
            int localStudents = (int) students.stream()
                    .filter(Student::getIsLocal) // Проверка, является ли студент местным
                    .count();

            System.out.printf("""
                            Общая статистика:
                            - Общее количество студентов: %s
                            - Количество местных студентов: %s
                            - Количество приезжих студентов: %s
                            - Город не указан: %s
                            
                            """,
                    totalStudents, localStudents, totalStudents - localStudents - unknownCityCount, unknownCityCount);

            for (Student student : students) {
                System.out.println(student.toString());
            }

            BDRepository.saveStudents(students);

        } catch (IOException | CsvException e) {
            System.out.println("Ошибка при обработке CSV: " + e.getMessage());
        } finally {
            BDRepository.disconnect();
        }
    }
}
