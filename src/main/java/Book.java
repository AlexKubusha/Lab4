import java.util.Objects;
import java.util.UUID;

/**
 * Клас, що представляє книгу з валідацією даних.
 * Базовий клас для ієрархії книг.
 * Реалізує інтерфейс Identifiable для підтримки унікальних ідентифікаторів.
 */
public abstract class Book implements Comparable<Book>, Identifiable {
    protected UUID uuid;
    protected String title;
    protected String author;
    protected int year;
    protected double price;
    protected Genre genre;

    /**
     * Конструктор для створення об'єкта Book.
     * @param title Назва книги
     * @param author Автор книги
     * @param year Рік видання
     * @param price Ціна книги
     * @param genre жанр книги
     */
    public Book(String title, String author, int year, double price, Genre genre) {
        this.uuid = UUID.randomUUID(); // Автоматична генерація UUID при створенні об'єкта
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPrice(price);
        this.genre = genre;
    }

    /**
     * Реалізація методу з інтерфейсу Identifiable.
     * @return унікальний ідентифікатор книги.
     */
    @Override
    public UUID getUuid() { return uuid; }

    public String getTitle() {
        return title;
    }

    /**
     * Встановлює назву книги.
     * @param title назва книги
     * @throws IllegalArgumentException якщо назва порожня або null
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва книги не може бути порожньою.");
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    /**
     * Встановлює автора книги.
     * @param author автор книги
     * @throws IllegalArgumentException якщо ім'я автора порожнє
     */
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Ім'я автора не може бути порожнім.");
        }
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    /**
     * Встановлює рік видання.
     * @param year рік
     * @throws IllegalArgumentException якщо рік менше 0 або більше 2026
     */
    public void setYear(int year) {
        if (year < 0 || year > 2026) {
            throw new IllegalArgumentException("Рік видання має бути в межах від 0 до 2026.");
        }
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Встановлює ціну книги.
     * @param price ціна
     * @throws IllegalArgumentException якщо ціна від'ємна
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною.");
        }
        this.price = price;
    }

    public Genre getGenre() { return genre; }

    /**
     * Встановлює жанр книги.
     * @param genre об'єкт перерахування Genre
     * @throws IllegalArgumentException якщо передано null значення жанру
     */
    public void setGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Жанр не може бути порожнім (null)");
        }
        this.genre = genre;
    }

    /**
     * Порівнює книги за їхньою назвою для сортування в алфавітному порядку.
     * @param other об'єкт книги для порівняння.
     * @return від'ємне число, нуль або додатне число залежно від результату порівняння назв.
     */
    @Override
    public int compareTo(Book other) {
        if (other == null) return 1;
        // Однозначне стабільне сортування за назвою (title)
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public String toString() {
        // Додано вивід UUID
        return String.format("ID: %s | Книга: '%s' | Автор: %s | Рік: %d | Ціна: %.2f | Жанр: %s",
                uuid.toString(),title, author, year, price, genre);
    }

    /**
     * Повертає скорочену інформацію про книгу для відображення у списку GUI.
     *
     */
    public String toShortString() {
        return String.format("%s: %s (Автор: %s) | UUID: %s",
                this.getClass().getSimpleName(), this.title, this.author, this.uuid.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Double.compare(book.price, price) == 0 &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(uuid, book.uuid); // Додано порівняння за UUID
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, title, author, year, price);
    }
}