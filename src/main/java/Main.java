import BD.BDRepository;
import BD.BdOrmRepository;
import BD.models.Student;
import visualisation.drawer.BarChartDrawer;
import visualisation.drawer.CityPieChartDrawer;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        var dbOrm = new BdOrmRepository();
        dbOrm.connect();

        try {
            System.setOut(new PrintStream(System.out, true, Charset.defaultCharset()));

            List<Student> students = dbOrm.getStudents();

//            for (Student student : students) {
//                System.out.println(student);
//            }

            CityPieChartDrawer.drawCityPieChart(students);
            BarChartDrawer.drawBarChart(students);

        } catch (SQLException e) {
            System.out.println("An error has occurred: " + e.getMessage());
        } finally {
            BDRepository.disconnect();
        }
    }
}