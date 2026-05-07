import java.util.ArrayList;
import java.util.List;

public class Library {
    private final List<LibraryItem> items = new ArrayList<>();

    /**
     * Додає книгу. Якщо книга з такою назвою, автором та класом вже є, збільшує кількість.
     */
    public void addNewBook(Book bk, int quantity) {
        for (LibraryItem item : items) {
            if (item.getBook().getClass().equals(bk.getClass()) &&
                    item.getBook().getTitle().equalsIgnoreCase(bk.getTitle()) &&
                    item.getBook().getAuthor().equalsIgnoreCase(bk.getAuthor())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new LibraryItem(bk, quantity));
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