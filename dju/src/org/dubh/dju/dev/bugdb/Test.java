import java.sql.*;
import postgres.util.UnixCrypt;

/**
 * Class to test JDBC connectivity to the database on wired
 * @author Brian Duff
 * @version 1.0
 */
class Test
{
   public static void main(String[] args)
   {
      try
      {
         Class.forName("postgresql.Driver");
         
         Connection db = DriverManager.getConnection("jdbc:postgresql://wired.dcs.st-and.ac.uk/briand",
                                                     "briand", "buniba65");
                                                     
         Statement st = db.createStatement();
         
         ResultSet res = st.executeQuery("select * from bg_operating_systems");
         
         while (res.next())
         {
            System.out.println(res.getString(1));
         }
         res.close();
         st.close();
      }
      catch (Throwable cnfe)
      {
         System.err.println("Got exception: "+cnfe);
      }   
   }
}