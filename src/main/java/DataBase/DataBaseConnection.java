package DataBase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//ruta :  data\dataBase.db
// Metodo VIEJO para sqllite:
public class DataBaseConnection {
    //    private static final String URL = "jdbc:sqlite:data/dataBase.db";
    //    private static java.sql.Connection conn;
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=AdminNegocio;integratedSecurity=true;encrypt=false;trustServerCertificate=true;";
    private static Connection conn;

    static {
        //try {
        //            File dbFolder = new File("data");
        //            if (!dbFolder.exists()) {
        //                dbFolder.mkdirs();
        //            }
        //            conn = DriverManager.getConnection("jdbc:sqlite:data/dataBase.db");
        //
        //        } catch (SQLException e) {
        try { conn = DriverManager.getConnection(URL); }
        catch (SQLException e)
        {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
    //public static java.sql.Connection getConnection() {
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al reconectar con la base de datos", e);
        }
        return conn;
    }

}

