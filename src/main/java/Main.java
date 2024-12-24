import BD.BDRepository;
import models.Student;
import visualisation.drawer.BarChartDrawer;
import visualisation.drawer.CityPieChartDrawer;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            System.setOut(new PrintStream(System.out, true, Charset.defaultCharset()));

            BDRepository.connect();

            List<Student> students = BDRepository.fetchStudents();

//            for (Student student : students) {
//                System.out.println(student);
//            }

            CityPieChartDrawer.drawCityPieChart(students);
            BarChartDrawer.drawBarChart(students);

        } finally {
            BDRepository.disconnect();
        }
    }
}