import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Library {
    private final List<LibraryItem> items = new ArrayList<>();

    /**
     * Додає книгу. Якщо книга з такою назвою, автором, класом, роком та ціною вже є, збільшує кількість.
     */
    public void addNewBook(Book bk, int quantity) {
        for (LibraryItem item : items) {
            if (item.getBook().getClass().equals(bk.getClass()) &&
                    item.getBook().getTitle().equalsIgnoreCase(bk.getTitle()) &&
                    item.getBook().getAuthor().equalsIgnoreCase(bk.getAuthor()) &&
                    item.getBook().getYear() == bk.getYear() &&
                    Double.compare(item.getBook().getPrice(), bk.getPrice()) == 0 &&
                    item.getBook().getGenre() == bk.getGenre()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new LibraryItem(bk, quantity));
    }

    /**
     * Повертає новий список, відсортований за допомогою Comparable.
     */
    public List<LibraryItem> getSortedItems() {
        List<LibraryItem> sortedList = new ArrayList<>(items);
        // Сортуємо, використовуючи перевизначений compareTo у класі Book
        Collections.sort(sortedList, (a, b) -> a.getBook().compareTo(b.getBook()));
        return sortedList;
    }

    public List<LibraryItem> getItems() { return items; }

    // --- Методи пошуку ---
    public List<LibraryItem> searchByAuthor(String author) {
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.getBook().getAuthor().toLowerCase().contains(author.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public List<LibraryItem> searchByYear(int year) {
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.getBook().getYear() >= year) {
                results.add(item);
            }
        }
        return results;
    }

    public List<LibraryItem> searchByMaxPrice(double maxPrice) {
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.getBook().getPrice() <= maxPrice) {
                results.add(item);
            }
        }
        return results;
    }
}