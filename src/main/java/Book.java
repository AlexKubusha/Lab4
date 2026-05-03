import java.util.Objects;

/**
 * Клас, що представляє книгу з валідацією даних.
 * Охоплює назву, автора, рік видання та ціну.
 * Містить статичний лічильник та конструктор копіювання.
 */
public class Book {
    protected String title;
    protected String author;
    protected int year;
    protected double price;
    protected Genre genre;


    /**
     * Конструктор для створення об'єкта Book.
     * @param title Назва книги (не може бути порожньою)
     * @param author Автор книги (не може бути порожнім)
     * @param year Рік видання (не може бути майбутнім або занадто старим)
     * @param price Ціна книги (не може бути від'ємною)
     * @throws IllegalArgumentException якщо вхідні дані некоректні
     * @param genre жанр книги (тип Genre)
     */
    public Book(String title, String author, int year, double price, Genre genre) {
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPrice(price);
        this.genre = genre;
    }

    /**
     * Конструктор копіювання.
     * Створює новий об'єкт на основі існуючого.
     * @param other об'єкт книги для копіювання
     */
    public Book(Book other) {
        if (other != null) {
            this.title = other.getTitle();
            this.author = other.getAuthor();
            this.year = other.getYear();
            this.price = other.getPrice();
            this.genre = other.getGenre();
        }
    }


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

    @Override
    public String toString() {
        return String.format("Книга: '%s' | Автор: %s | Рік: %d | Ціна: %.2f | Жанр: %s",
                title, author, year, price, genre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Double.compare(book.price, price) == 0 &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, year, price);
    }
}