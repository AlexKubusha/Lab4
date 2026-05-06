/**
 * Похідний клас для аудіокниги.
 */
public class AudioBook extends Book {
    private int durationMinutes;

    public AudioBook(String title, String author, int year, double price, Genre genre, int durationMinutes) {
        super(title, author, year, price, genre);
        setDurationMinutes(durationMinutes);
    }

    public int getDurationMinutes() { return durationMinutes; }

    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) throw new IllegalArgumentException("Тривалість має бути додатною.");
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | [Audio] Тривалість: %d хв", durationMinutes);
    }
}
