import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Драйвер програми для керування книгами.
 * Реалізує консольне меню, динамічний список ArrayList та обробку винятків.
 */
public class Main {
    private static final List<Book> books = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("Вітаємо у системі керування бібліотекою!");

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addBook();
                case "2" -> printBooks();
                case "3" -> {
                    running = false;
                    System.out.println("\nДякуємо за використання! Програму завершено.");
                }
                default -> System.out.println("Помилка: Оберіть пункт меню від 1 до 3.");
            }
        }
    }

    /**
     * Виводить головне меню програми.
     */
    private static void printMenu() {
        System.out.println("\n========= МЕНЮ =========");
        System.out.println("1. Створити новий об’єкт (Додати книгу)");
        System.out.println("2. Вивести інформацію про всі об’єкти");
        System.out.println("3. Завершити роботу");
        System.out.print("Ваш вибір: ");
    }

    /**
     * Метод для додавання книги з обробкою винятків.
     */
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

            Book newBook = new Book(title, author, year, price);
            books.add(newBook);
            System.out.println("Книгу успішно додано до списку!");

        } catch (NumberFormatException e) {
            System.out.println("Помилка: Рік та ціна повинні бути числами!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації: " + e.getMessage());
        }
    }

    /**
     * Виводить список усіх доданих книг.
     */
    private static void printBooks() {
        if (books.isEmpty()) {
            System.out.println("\nСписок книг поки що порожній.");
        } else {
            System.out.println("\n--- Ваші книги у списку ---");
            books.forEach(System.out::println);
        }
    }
}
