package DataBase;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

//ruta :  data\dataBase.db
public class Connection {
    private static final String URL = "jdbc:sqlite:data/dataBase.db";
    private static java.sql.Connection conn;

    static {
        try {
            File dbFolder = new File("data");
            if (!dbFolder.exists()) {
                dbFolder.mkdirs();
            }
            conn = DriverManager.getConnection("jdbc:sqlite:data/dataBase.db");

        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    public static java.sql.Connection getConnection() {
        return conn;
    }
}

