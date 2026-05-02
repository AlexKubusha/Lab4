import java.util.ArrayList;
import java.util.List;

/**
 * Клас Library демонструє принцип агрегації.
 * Він "володіє" списком об'єктів Book, але книги можуть існувати і поза бібліотекою.
 */
public class Library {
    private String name;
    private List<Book> books;

    /**
     * Конструктор для створення об'єкта Library.
     * @param name Назва бібліотеки
     */
    public Library(String name) {
        setName(name);
        this.books = new ArrayList<>();
    }

    /**
     * Додає книгу до бібліотеки (Агрегація).
     * @param book об'єкт класу Book
     * @throws IllegalArgumentException якщо передано null
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Неможливо додати порожній об'єкт книги.");
        }
        this.books.add(book);
    }

    /**
     * Виводить повний каталог книг бібліотеки.
     */
    public void displayCatalog() {
        System.out.println("\nБібліотечний каталог: " + name);
        if (books.isEmpty()) {
            System.out.println("Наразі у бібліотеці немає книг.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    /**
     * Повертає кількість книг у цій конкретній бібліотеці.
     * @return розмір списку книг
     */
    public int getBooksCount() {
        return books.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва бібліотеки не може бути порожньою.");
        }
        this.name = name;
    }
}