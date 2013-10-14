package namingtool;

import java.sql.*;

public class DBTools {
    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:namingtooldb;create=true";
    
    private static DBTools instance;    
    
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;   
    
    public String executeQuery(String query) {        
        try {            
            stmt = con.createStatement();        
            stmt.execute(query);            
        } catch (SQLException ex) {
            //Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
        return "OK";
        
    }
    
    public ResultSet sendQuery(String str){
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(str);                                                
            return rs;            
        } catch (SQLException ex) {
            //Logger.getLogger(DBTools.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return null;
    }    
    
    public static DBTools getInstance(){
        if (instance == null){
            instance = new DBTools();
        }        
        instance.connect();
        return instance;                
    }
    
    private DBTools(){        
        connect();
    }
    
    private void connect() {        
        try {
            Class.forName(DRIVER).newInstance();
            con = DriverManager.getConnection(JDBC_URL); 
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void disconnect() {        
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
