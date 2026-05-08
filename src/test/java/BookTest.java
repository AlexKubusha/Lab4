import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Клас для тестування валідації та винятків.
 */
public class BookTest {

    @Test
    void shouldThrowInvalidBookDataExceptionWhenPriceIsNegative() {
        // Перевірка кидання власного винятку в конструкторі при від'ємній ціні
        assertThrows(InvalidBookDataException.class, () -> {
            new PaperBook("Назва", "Автор", 2020, -10.0, Genre.FICTION, 300);
        });
    }

    @Test
    void shouldThrowInvalidBookDataExceptionWhenTitleIsEmpty() {
        // Перевірка кидання власного винятку при порожній назві
        assertThrows(InvalidBookDataException.class, () -> {
            new PaperBook("", "Автор", 2020, 100.0, Genre.NON_FICTION, 500);
        });
    }

    @Test
    void shouldThrowInvalidBookDataExceptionWhenFutureYear() {
        // Перевірка року (макс 2026 за логікою в Book.java)
        assertThrows(InvalidBookDataException.class, () -> {
            new PaperBook("Future Book", "Unknown", 2050, 10.0, Genre.HISTORY, 400);
        });
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenDeletingNonExistingObject() {
        // Тест для агрегуючого класу Library
        Library emptyLib = new Library();
        LibraryItem fakeItem = new LibraryItem(
                new PaperBook("Test", "Author", 2000, 100, Genre.HISTORY, 500), 1
        );

        // Перевірка, що метод delete кидає BookNotFoundException
        assertThrows(BookNotFoundException.class, () -> {
            emptyLib.delete(fakeItem);
        });
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenSearchingInvalidUuid() {
        Library lib = new Library();
        // Перевірка, що пошук за неіснуючим або невірним UUID кидає виняток
        assertThrows(BookNotFoundException.class, () -> {
            lib.searchByUuid("invalid-uuid-format-123");
        });
    }
}