
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author ethanbloom
 */
public class ClassQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement addClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement dropClass;
    private static ResultSet resultSet; // used for queries
    
    public static void addClass(ClassEntry aClass)
    {
        connection = DBConnection.getConnection();
        try
        {
            addClass = connection.prepareStatement(
      "insert into APP.CLASS (semester,coursecode,seats) values (?,?,?)"
            );
            addClass.setString(1, aClass.getSemester());
            addClass.setString(2, aClass.getCourseCode());
            addClass.setInt(3, aClass.getSeats());
            addClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        try
        {
            getAllCourseCodes = connection.prepareStatement(
            "select COURSECODE from APP.CLASS where SEMESTER = ?"
            );
            getAllCourseCodes.setString(1, semester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                String aCourseCode = resultSet.getString(1);
                courseCodes.add(aCourseCode);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseCodes;
    }
    
    public static int getClassSeats(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int numseats = 0;
        try
        {
            getClassSeats = connection.prepareStatement(
            "select SEATS from APP.CLASS where SEMESTER = ? and COURSECODE = ?"
            );
            getClassSeats.setString(1, semester);
            getClassSeats.setString(2, courseCode);
            resultSet = getClassSeats.executeQuery();
            
            while(resultSet.next())
            {
                numseats = resultSet.getInt(1);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return numseats;
    }
    
    public static void dropClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropClass = connection.prepareStatement(
            "delete from APP.CLASS where semester = ? and coursecode = ?"
            );
            dropClass.setString(1, semester);
            dropClass.setString(2, courseCode);
            dropClass.executeUpdate();

        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
