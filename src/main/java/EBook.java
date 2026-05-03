/**
 * Похідний клас для електронної книги.
 */
public class EBook extends Book {
    private double fileSizeMb;

    public EBook(String title, String author, int year, double price, Genre genre, double fileSizeMb) {
        super(title, author, year, price, genre);
        setFileSizeMb(fileSizeMb);
    }

    public double getFileSizeMb() { return fileSizeMb; }

    public void setFileSizeMb(double fileSizeMb) {
        if (fileSizeMb <= 0) throw new IllegalArgumentException("Розмір файлу має бути позитивним.");
        this.fileSizeMb = fileSizeMb;
    }

    @Override
    public String toString() {
        // Використання super.toString() для поліморфного виводу
        return super.toString() + String.format(" | [E-Book] Розмір: %.1f MB", fileSizeMb);
    }
}