package visualisation.drawer;

import BD.models.Student;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BarChartDrawer {

    public static void drawBarChart(List<Student> students) {

        // Фильтруем студентов, у которых указан город
        List<Student> studentsWithCity = students.stream()
                .filter(student -> !student.getCity().equals("город студента не указан"))
                .toList();

        // Разделяем на местных и не местных студентов
        Map<String, List<Student>> groupedByLocality = studentsWithCity.stream()
                .collect(Collectors.groupingBy(student -> student.getIsLocal() ? "Местные" : "Не местные"));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Группируем по IsLocal
        for (Map.Entry<String, List<Student>> entry : groupedByLocality.entrySet()) {
            String locality = entry.getKey();
            List<Student> studentGroup = entry.getValue();

            // Считаем средний балл для каждой группы
            double averageScore = studentGroup
                    .stream()
                    .mapToInt(Student::getTotalScore)
                    .average()
                    .orElse(0.0);

            dataset.addValue(averageScore, locality, locality);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "У кого лучше успеваемость, у местных или приезжих?",
                "Группа студентов",
                "Средний балл",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Настроим плотность столбиков
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.getRenderer().setSeriesItemLabelGenerator(0, null);
        plot.getDomainAxis().setCategoryMargin(0.3);
        plot.getDomainAxis().setLowerMargin(0.1);
        plot.getDomainAxis().setUpperMargin(0.2);

        JFrame frame = new JFrame("Столбчатая диаграмма");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();

        // Устанавливаем окно по центру экрана
        frame.setLocationRelativeTo(null);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}