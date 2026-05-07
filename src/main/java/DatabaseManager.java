import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private String url;
    private String user;
    private String password;

    public DatabaseManager(String configPath) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            props.load(fis);
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Помилка зчитування конфігурації: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void saveBook(Book book, int quantity) {
        String sql = "INSERT INTO books (type, title, author, year_published, price, genre," +
                "file_size_mb, weight_grams, duration_minutes, book_condition, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getClass().getSimpleName());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getYear());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setString(6, book.getGenre().name());

            // Встановлюємо Null для всіх специфічних полів
            pstmt.setNull(7, java.sql.Types.DOUBLE);
            pstmt.setNull(8, java.sql.Types.INTEGER);
            pstmt.setNull(9, java.sql.Types.INTEGER);
            pstmt.setNull(10, java.sql.Types.VARCHAR);

            // Заповнюємо залежно від реального класу
            if (book instanceof EBook) {
                pstmt.setDouble(7, ((EBook) book).getFileSizeMb());
            } else if (book instanceof PaperBook) {
                pstmt.setInt(8, ((PaperBook) book).getWeightGrams());
            } else if (book instanceof AudioBook) {
                pstmt.setInt(9, ((AudioBook) book).getDurationMinutes());
            } else if (book instanceof RareBook) {
                pstmt.setString(10, ((RareBook) book).getCondition());
            }

            pstmt.setInt(11, quantity);

            pstmt.executeUpdate();
            System.out.println("[БД] Запис додано до таблиці.");
        } catch (SQLException e) {
            System.err.println("Помилка SQL: " + e.getMessage());
        }
    }
}