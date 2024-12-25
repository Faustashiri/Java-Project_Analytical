package visualisation.drawer;

import BD.BdOrmRepository;
import BD.models.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindow {

    private static final Color defaultColor = new Color(100, 150, 255);
    private static final Color exitButtonColor = new Color(255, 100, 100);

    public static void main(String[] args) throws Exception {

        var dbOrm = new BdOrmRepository();
        dbOrm.connect();

        try {
            List<Student> studentsFromDB = dbOrm.getStudents();

            List<Student> filteredStudents = filterStudents(studentsFromDB);

            JFrame mainFrame = new JFrame("У кого лучше успеваемость, у местных или приезжих?");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(500, 250);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.getContentPane().setBackground(new Color(240, 240, 240)); // Фон окна

            JLabel titleLabel = new JLabel("Выберите тип анализа для отображения", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(Color.DARK_GRAY);
            mainFrame.add(titleLabel, BorderLayout.NORTH);
            titleLabel.setBorder(new EmptyBorder(20, 10, 20, 10));

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
            buttonPanel.setBackground(new Color(240, 240, 240));

            ButtonStyle barButton = new ButtonStyle("Столбчатая диаграмма", defaultColor);
            ButtonStyle cityButton = new ButtonStyle("Круговая диаграмма", defaultColor);

            Dimension buttonSize = new Dimension(200, 38);
            barButton.setPreferredSize(buttonSize);
            cityButton.setPreferredSize(buttonSize);

            barButton.addActionListener(_ -> {
                BarChartDrawer.drawBarChart(filteredStudents);
            });

            cityButton.addActionListener(e -> {
                CityPieChartDrawer.drawCityPieChart(studentsFromDB);
            });

            buttonPanel.add(barButton);
            buttonPanel.add(cityButton);

            ButtonStyle exitButton = new ButtonStyle("Выйти", exitButtonColor);
            exitButton.setPreferredSize(new Dimension(120, 40));
            exitButton.addActionListener(e -> System.exit(0));

            JPanel exitButtonPanel = new JPanel();
            exitButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            exitButtonPanel.setBackground(new Color(240, 240, 240));
            exitButtonPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
            exitButtonPanel.add(exitButton);

            mainFrame.add(buttonPanel, BorderLayout.CENTER);
            mainFrame.add(exitButtonPanel, BorderLayout.SOUTH);

            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Произошла ошибка: " + e.getMessage());
        } finally {
            dbOrm.disconnect();
        }
    }

    private static List<Student> filterStudents(List<Student> students) {
        return students.stream()
                .filter(student -> !student.getCity().isEmpty()
                        && !student.getCity().equals("город студента не указан"))
                .collect(Collectors.toList());
    }
}