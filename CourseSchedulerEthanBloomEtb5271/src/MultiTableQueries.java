
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author ethanbloom
 */
public class MultiTableQueries {
    private static Connection connection;
    private static PreparedStatement getCodeAndDescription;
    private static PreparedStatement getSeats;
    private static PreparedStatement getScheduledStudentsByClass;
    private static PreparedStatement getScheduledStudentEntries;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static PreparedStatement getWaitlistedtudentEntries;
    private static ResultSet resultSetDescription;
    private static ResultSet resultSetSeats;
    private static ResultSet resultSetStudents;
    private static ResultSet resultSetStudentInfo;
    
    public static ArrayList<ClassDescription> getAllClassDescriptions(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<>();
        ArrayList<ClassDescription> classDescriptions = new ArrayList<>();
        courseCodes = ClassQueries.getAllCourseCodes(semester);
        for (String courseCode : courseCodes)
        {
            try
            {
                getCodeAndDescription = connection.prepareStatement(
                "select DESCRIPTION from APP.COURSE where COURSECODE = ?"
                );
                getCodeAndDescription.setString(1, courseCode);
                getSeats = connection.prepareStatement(
                "select SEATS from APP.CLASS where COURSECODE = ?"
                );
                getSeats.setString(1, courseCode);
                resultSetDescription = getCodeAndDescription.executeQuery();
                resultSetSeats = getSeats.executeQuery();
                
                
                while(resultSetDescription.next() && resultSetSeats.next())
                {
                    String description = resultSetDescription.getString(1);
                    int seats = resultSetSeats.getInt(1);
                    classDescriptions.add(new ClassDescription(courseCode, description, seats));
                }
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
        }
        return classDescriptions;
    }
    
    public static ArrayList<StudentEntry> getScheduledStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> studentIDs = new ArrayList<>();
        ArrayList<StudentEntry> scheduledStudents = new ArrayList<>();
        try
        {
            // First, get all student ID's with status = 's' from app.schedule
            
            getScheduledStudentsByClass = connection.prepareStatement(
            "select STUDENTID, STATUS from APP.SCHEDULE where SEMESTER = ? and COURSECODE = ?"
            );
            getScheduledStudentsByClass.setString(1, semester);
            getScheduledStudentsByClass.setString(2, courseCode);
            resultSetStudents = getScheduledStudentsByClass.executeQuery();
            
            while(resultSetStudents.next())
            {
                String status = resultSetStudents.getString(2);
                if("s".equals(status))
                {
                    studentIDs.add(resultSetStudents.getString(1));
                }
            }
            // Then, get student info from app.student and add studententries
            for (String studentID : studentIDs)
            {
                try
                {
                    getScheduledStudentEntries = connection.prepareStatement(
                    "select FIRSTNAME, LASTNAME from APP.STUDENT where STUDENTID = ?"
                    );
                    getScheduledStudentEntries.setString(1, studentID);
                    resultSetStudentInfo = getScheduledStudentEntries.executeQuery();
                    
                    while(resultSetStudentInfo.next())
                    {
                        String firstName = resultSetStudentInfo.getString(1);
                        String lastName = resultSetStudentInfo.getString(2);
                        scheduledStudents.add(new StudentEntry(studentID, firstName, lastName));
                    }
                }
                catch(SQLException sqlException)
                {
                    sqlException.printStackTrace();
                }
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return scheduledStudents;
    }
    
    public static ArrayList<StudentEntry> getWaitlistedStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> studentIDs = new ArrayList<>();
        ArrayList<StudentEntry> waitlistedStudents = new ArrayList<>();
        try
        {
            // First, get all student ID's with status = 's' from app.schedule
            
            getWaitlistedStudentsByClass = connection.prepareStatement(
            "select STUDENTID, STATUS from APP.SCHEDULE where SEMESTER = ? and COURSECODE = ?"
            );
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, courseCode);
            resultSetStudents = getWaitlistedStudentsByClass.executeQuery();
            
            while(resultSetStudents.next())
            {
                String status = resultSetStudents.getString(2);
                if("w".equals(status))
                {
                    studentIDs.add(resultSetStudents.getString(1));
                }
            }
            // Then, get student info from app.student and add studententries
            for (String studentID : studentIDs)
            {
                try
                {
                    getWaitlistedtudentEntries = connection.prepareStatement(
                    "select FIRSTNAME, LASTNAME from APP.STUDENT where STUDENTID = ?"
                    );
                    getWaitlistedtudentEntries.setString(1, studentID);
                    resultSetStudentInfo = getWaitlistedtudentEntries.executeQuery();
                    
                    while(resultSetStudentInfo.next())
                    {
                        String firstName = resultSetStudentInfo.getString(1);
                        String lastName = resultSetStudentInfo.getString(2);
                        waitlistedStudents.add(new StudentEntry(studentID, firstName, lastName));
                    }
                }
                catch(SQLException sqlException)
                {
                    sqlException.printStackTrace();
                }
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlistedStudents;
    }
}
