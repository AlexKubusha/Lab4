/**
 * Допоміжний клас для зберігання об'єкта книги та її кількості в бібліотеці.
 */
public class LibraryItem {
    private Book book;
    private int quantity;

    public LibraryItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return book.toString() + " | Кількість: " + quantity;
    }
}