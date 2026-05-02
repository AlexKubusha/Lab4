import java.util.Scanner;

/**
 * Драйвер програми для керування книгами.
 * Демонструє агрегацію, роботу з enum, статичними членами та конструктором копіювання.
 */

public class Main {
    // Агрегація: замість простого списку використовуємо об'єкт класу Library
    private static final Library myLibrary = new Library("Центральна бібліотека СумДУ");
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("Вітаємо у системі керування бібліотекою!");

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addBook();
                case "2" -> myLibrary.displayCatalog(); // Виклик методу агрегатора
                case "3" -> showStatistics(); // Тестування статичного поля
                case "4" -> testCopyConstructor(); // Тестування конструктора копіювання
                case "5" -> {
                    running = false;
                    System.out.println("\nДякуємо за використання! Програму завершено.");
                }
                default -> System.out.println("⚠Помилка: Оберіть пункт меню від 1 до 5.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========= МЕНЮ =========");
        System.out.println("1. Додати нову книгу");
        System.out.println("2. Вивести весь каталог бібліотеки");
        System.out.println("3. Показати статистику (статичне поле)");
        System.out.println("4. Протестувати конструктор копіювання");
        System.out.println("5. Завершити роботу");
        System.out.print("Ваш вибір: ");
    }

    private static void addBook() {
        try {
            System.out.println("\n--- Введення даних для нової книги ---");

            System.out.print("Назва: ");
            String title = scanner.nextLine();

            System.out.print("Автор: ");
            String author = scanner.nextLine();

            System.out.print("Рік видання: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Ціна: ");
            double price = Double.parseDouble(scanner.nextLine());

            Genre selectedGenre = chooseGenre();

            Book newBook = new Book(title, author, year, price, selectedGenre);
            myLibrary.addBook(newBook); // Агрегування об'єкта
            System.out.println("Книгу успішно додано до бібліотеки!");

        } catch (NumberFormatException e) {
            System.out.println("Помилка: Рік та ціна повинні бути числами!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації: " + e.getMessage());
        }
    }

    /**
     * Допоміжний метод для вибору значення Enum.
     */
    private static Genre chooseGenre() {
        System.out.println("Оберіть жанр: 1. Художня, 2. Наукова, 3. Фентезі, 4. Історія, 5. Технічна");
        System.out.print("Ваш вибір: ");
        String choice = scanner.nextLine();
        return switch (choice) {
            case "1" -> Genre.FICTION;
            case "2" -> Genre.NON_FICTION;
            case "3" -> Genre.FANTASY;
            case "4" -> Genre.HISTORY;
            default -> Genre.TECHNICAL;
        };
    }

    /**
     * Демонстрація роботи статичного поля та методу.
     */
    private static void showStatistics() {
        System.out.println("\n--- СТАТИСТИКА ОБ'ЄКТІВ ---");
        System.out.println("Загальна кількість створених екземплярів Book: " + Book.getTotalBooksCreated());
        System.out.println("Кількість книг саме у вашій бібліотеці: " + myLibrary.getBooksCount());
    }

    /**
     * Демонстрація роботи конструктора копіювання.
     */
    private static void testCopyConstructor() {
        try {
            Book original = new Book("Оригінал", "Автор", 2024, 500.0, Genre.FICTION);
            Book copy = new Book(original); // Виклик конструктора копіювання

            System.out.println("\n--- Тест конструктора копіювання ---");
            System.out.println("Оригінал: " + original);
            System.out.println("Копія:    " + copy);
            System.out.println("Об'єкти однакові за змістом? " + original.equals(copy));
            System.out.println("Це один і той самий об'єкт в пам'яті? " + (original == copy));
        } catch (Exception e) {
            System.out.println("Помилка тестування: " + e.getMessage());
        }
    }
}