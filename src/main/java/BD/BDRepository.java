package BD;

import models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BDRepository {
    private static final String DB_URL = "jdbc:sqlite:students.db";
    private static Connection dbConnection = null;

    public static void connect() {
        try {
            dbConnection = DriverManager.getConnection(DB_URL);
            System.out.println("The connection  is established.");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public static void disconnect() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
                System.out.println("The connection to the database is closed.");
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
            }
        }
    }

    public static void createStudentsTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    ulearnID TEXT,
                    studentGroup TEXT,
                    isLocal BOOLEAN,
                    city TEXT,
                    totalScore INTEGER
                )
                """;

        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(sql);
            System.out.println("The table has been created.");
        } catch (SQLException e) {
            System.out.println("Error creating the table: " + e.getMessage());
        }
    }

    public static void saveStudents(List<Student> students) {
        String sql = """
            INSERT INTO students (name, ulearnID, studentGroup, isLocal, city, totalScore)
            VALUES (?, ?, ?, ?, ?, ?)
        """;


        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            for (Student student : students) {
                pstmt.setString(1, student.getName());
                pstmt.setString(2, student.getUlearnId());
                pstmt.setString(3, student.getGroup());
                pstmt.setBoolean(4, student.getIsLocal());
                pstmt.setString(5, student.getCity());
                pstmt.setInt(6, student.getTotalScore());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("The data is saved.");
        } catch (SQLException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static List<Student> fetchStudents() {

        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT * FROM students"; // SQL-запрос для получения всех студентов

        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Получаем данные из ResultSet
                String name = rs.getString("name");
                String ulearnId = rs.getString("ulearnID");
                String studentGroup = rs.getString("studentGroup");
                boolean isLocal = rs.getBoolean("isLocal");
                String city = rs.getString("city");
                int totalScore = rs.getInt("totalScore");

                studentList.add(new Student(name, ulearnId, studentGroup, isLocal, totalScore, city));

            }
        } catch (SQLException e) {
            System.out.println("Error fetching student data: " + e.getMessage());
        }

        return studentList;
    }
}