import BD.BDRepository;
import models.Student;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            System.setOut(new PrintStream(System.out, true, Charset.defaultCharset()));

            BDRepository.connect();

            List<Student> students = BDRepository.fetchStudents();

            for (Student student : students) {
                System.out.println(student);
            }
        } finally {
            BDRepository.disconnect();
        }
    }
}