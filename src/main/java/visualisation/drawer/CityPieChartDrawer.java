package visualisation.drawer;

import BD.models.Student;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;

public class CityPieChartDrawer {

    public static void drawCityPieChart(List<Student> students) {

        DefaultPieDataset dataset = new DefaultPieDataset();

        // Студенты с указанным городом
        long withCity = students.stream()
                .filter(student -> !student.getCity().equals("город студента не указан"))
                .count();

        // Студенты без указанного города
        long withoutCity = students.stream()
                .filter(student -> student.getCity().equals("город студента не указан"))
                .count();

        dataset.setValue("Город указан", withCity);
        dataset.setValue("Город не указан", withoutCity);

        JFreeChart chart = ChartFactory.createPieChart(
                "Статистика по городам студентов",
                dataset,
                true,
                true,
                false
        );

        // Получаем объект PiePlot для настройки отображения
        PiePlot plot = (PiePlot) chart.getPlot();

        // Настройка отображения только процентов
        plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator(
                "{2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));

        // Изменяем шрифт текста в легенде
        LegendTitle legend = chart.getLegend();
        legend.setItemFont(new Font("Arial", Font.BOLD, 14)); // Шрифт: Arial, полужирный, размер 14

        // Отображаем диаграмму в окне
        JFrame frame = new JFrame("Круговая диаграмма по городам");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();

        // Устанавливаем окно по центру экрана
        frame.setLocationRelativeTo(null);

        // Отображаем окно
        frame.setSize(800, 600);
        frame.setVisible(true);

    }
}