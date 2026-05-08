import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

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
     * Видаляє об'єкт із колекції за посиланням.
     * @return true, якщо видалено успішно.
     */
    public boolean delete(LibraryItem itemToDelete) {
        if (itemToDelete == null) return false;
        return items.remove(itemToDelete);
    }

    /**
     * Оновлює існуючий об'єкт новими даними.
     * @return true, якщо об'єкт знайдено та оновлено.
     */
    public boolean update(LibraryItem existingItem, Book newBookData) {
        if (existingItem == null || newBookData == null) return false;

        int index = items.indexOf(existingItem);
        if (index != -1) {
            // Замінюємо старий елемент новим із тими ж даними про кількість
            items.set(index, new LibraryItem(newBookData, existingItem.getQuantity()));
            return true;
        }
        return false;
    }

    /**
     * Пошук об'єкта за його унікальним ідентифікатором UUID.
     * @param uuidString рядок, що представляє UUID.
     * @return знайдений LibraryItem або null, якщо об'єкт не знайдено або формат некоректний.
     */
    public LibraryItem searchByUuid(String uuidString) {
        try {
            UUID searchId = UUID.fromString(uuidString.trim());
            for (LibraryItem item : items) {
                if (item.getBook().getUuid().equals(searchId)) {
                    return item;
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Помилка: Некоректний формат UUID.");
        }
        return null;
    }

    /**
     * Повертає відсортований список згідно з переданим компаратором.
     */
    public List<LibraryItem> getSortedItems(Comparator<LibraryItem> comparator) {
        List<LibraryItem> sortedList = new ArrayList<>(this.items);
        Collections.sort(sortedList, comparator);
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