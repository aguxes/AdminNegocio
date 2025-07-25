package DataBase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// ruta :  data\dbNegocio.db
public class DataBaseConnection {
        private static final String URL = "jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres";
        private static final String USER = "postgres.fxyzekhrzihgojvgdyfl";
            private static final String PASSWORD = "dblolNegocio_1";
        private static Connection conn;
    static {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a Supabase PostgreSQL");

        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
        public static Connection getConnection() {
            try {
                if (conn == null || conn.isClosed()) conn = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException("Error al reconectar con la base de datos", e);
            }
            return conn;
        }
}