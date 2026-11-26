package bank.dao;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:h2:./data/bankdb";

    private Database() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, "sa", "");
    }

    // Reads schema.sql and creates the tables if they don't exist yet
    public static void createTables() {
        try (InputStream in = Database.class.getResourceAsStream("/schema.sql")) {
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection c = getConnection(); Statement st = c.createStatement()) {
                for (String part : sql.split(";")) {
                    if (!part.isBlank()) {
                        st.execute(part);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not create tables: " + e.getMessage(), e);
        }
    }
}