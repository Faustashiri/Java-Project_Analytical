package BD;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import BD.models.Student;

import java.sql.SQLException;
import java.util.List;

public class BdOrmRepository {

    private ConnectionSource connectionSource = null;
    private Dao<Student, Integer> studentDao = null;


    public void connect() throws SQLException {
        String DB_URL = "jdbc:sqlite:students.db";
        connectionSource = new JdbcConnectionSource(DB_URL);
        studentDao = DaoManager.createDao(connectionSource, Student.class);
        System.out.println("The connection to the database is established.\n");
    }

    public void disconnect() throws Exception {
        if (connectionSource != null) {
            connectionSource.close();
            System.out.println("The database connection is closed.\n");
        }
    }

    public void createTableStudents() throws SQLException {
        TableUtils.createTable(connectionSource, Student.class);
        System.out.println("The students table has been created.\n");
    }

    public void saveStudent(List<Student> students) throws SQLException {
        for (Student student : students) {
            studentDao.create(student); // Сохраняем каждого студента
        }
        System.out.println("The students' data is stored in the database.\n");
    }

    public List<Student> getStudents() throws SQLException {
        return studentDao.queryForAll();
    }
}