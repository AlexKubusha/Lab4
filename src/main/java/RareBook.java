/**
 * Похідний клас для рідкісної книги.
 */
public class RareBook extends Book {
    private String condition;

    public RareBook(String title, String author, int year, double price, Genre genre, String condition) {
        super(title, author, year, price, genre);
        setCondition(condition);
    }

    public String getCondition() { return condition; }

    public void setCondition(String condition) {
        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Стан книги не може бути порожнім.");
        }
        this.condition = condition;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | [Rare] Стан: %s", condition);
    }
}