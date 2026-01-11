package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String URL = "jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres";
    private static final String USER = "postgres.fxyzekhrzihgojvgdyfl";
    private static final String PASSWORD = "dblolNegocio_1";
    private static Connection conn;

    static {
        try {
            // Carga explícita del driver para asegurar compatibilidad
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a Supabase PostgreSQL");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error CRÍTICO: No se encontró el driver de PostgreSQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error CRÍTICO: No se pudo conectar a la base de datos.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("🔄 Re-conectando a la base de datos...");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al reconectar: " + e.getMessage());
        }
        return conn;
    }
}