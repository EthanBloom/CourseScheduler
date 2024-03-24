
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author ethanbloom
 */
public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllStudents;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    public static void addStudent(StudentEntry student)
    {
        connection = DBConnection.getConnection();
        try
        {
            addStudent = connection.prepareStatement(
      "insert into APP.STUDENT (studentID,firstname,lastname) values (?,?,?)"
            );
            addStudent.setString(1,student.getStudentID());
            addStudent.setString(2, student.getFirstName());
            addStudent.setString(3, student.getLastName());
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<StudentEntry> getAllStudents()
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            getAllStudents = connection.prepareStatement(
      "select STUDENTID, FIRSTNAME, LASTNAME from APP.STUDENT"
            );
            resultSet = getAllStudents.executeQuery();
            
            while(resultSet.next())
            {
                String studentID = resultSet.getString(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                students.add(new StudentEntry(studentID, firstName, lastName));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
    public static StudentEntry getStudent(String studentID)
    {
        connection = DBConnection.getConnection();
        try
        {
            getStudent = connection.prepareStatement(
      "select STUDENTID, FIRSTNAME, LASTNAME from APP.STUDENT where STUDENTID = ?"
            );
            getStudent.setString(1,studentID);
            resultSet = getStudent.executeQuery();
            resultSet.next();
            return new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
            return null;
        }
    }
    
    public static void dropStudent(String studentID)
    {
        connection = DBConnection.getConnection();
        try
        {
            getStudent = connection.prepareStatement(
      "delete from APP.STUDENT where STUDENTID = ?"
            );
            getStudent.setString(1,studentID);
            getStudent.executeUpdate();      
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
