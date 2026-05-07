import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Клас для тестування валідації класу Book.
 */
public class BookTest {
    @Test
    void shouldThrowExceptionWhenInvalidPriceInSetter() {
        // Створюємо спочатку валідний об'єкт
        // Book book = new Book("Clean Code", "Robert Martin", 2008, 50.0, Genre.FICTION);

        // Перевіряємо, що сетер викине помилку при від'ємній ціні
        assertThrows(IllegalArgumentException.class, () -> {
            //book.setPrice(-10.5);
        });
    }

    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        // Перевіряємо, що конструктор викине помилку при порожній назві
        assertThrows(IllegalArgumentException.class, () -> {
           // new Book("", "Author", 2020, 100.0, Genre.NON_FICTION);
        });
    }

    @Test
    void shouldThrowExceptionWhenFutureYear() {
        // Додатковий тест: перевірка року
        assertThrows(IllegalArgumentException.class, () -> {
           // new Book("Future Book", "Unknown", 2050, 10.0, Genre.HISTORY);
        });
    }
}
