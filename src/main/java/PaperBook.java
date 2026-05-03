/**
 * Похідний клас для паперової книги.
 */
public class PaperBook extends Book {
    private int weightGrams;

    public PaperBook(String title, String author, int year, double price, Genre genre, int weightGrams) {
        super(title, author, year, price, genre);
        setWeightGrams(weightGrams);
    }

    public int getWeightGrams() { return weightGrams; }

    public void setWeightGrams(int weightGrams) {
        if (weightGrams <= 0) throw new IllegalArgumentException("Вага має бути більшою за 0.");
        this.weightGrams = weightGrams;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | [Paper] Вага: %d г", weightGrams);
    }
}
