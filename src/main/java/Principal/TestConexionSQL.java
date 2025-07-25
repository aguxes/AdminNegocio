package Principal;

import DataBase.DataBaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexionSQL {
    public static void main(String[] args) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa a Supabase PostgreSQL");
            } else {
                System.out.println("⚠️ La conexión está cerrada o es nula.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Otro error: " + e.getMessage());
        }
    }
}