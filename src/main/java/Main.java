import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Драйвер програми для керування бібліотекою.
 * Демонструє принципи успадкування та поліморфізму на прикладі класів Book, EBook та PaperBook.
 */
public class Main {
    private static final List<Book> books = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("Вітаємо у системі керування бібліотекою (Практична робота №7)!");

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addBook(1); // Звичайна книга
                case "2" -> addBook(2); // EBook
                case "3" -> addBook(3); // PaperBook
                case "4" -> printAllBooks(); // Вивід через поліморфізм
                case "5" -> {
                    running = false;
                    System.out.println("\nДякуємо за використання! Програму завершено.");
                }
                default -> System.out.println("Помилка: Оберіть пункт меню від 1 до 5.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========= МЕНЮ =========");
        System.out.println("1. Додати звичайну книгу");
        System.out.println("2. Додати електронну книгу (EBook)");
        System.out.println("3. Додати паперову книгу (PaperBook)");
        System.out.println("4. Вивести весь список книг");
        System.out.println("5. Завершити роботу");
        System.out.print("Ваш вибір: ");
    }

    /**
     * Універсальний метод для створення об'єктів різних типів.
     * @param type 1 - Book, 2 - EBook, 3 - PaperBook
     */
    private static void addBook(int type) {
        try {
            System.out.println("\n--- Введення даних ---");

            System.out.print("Назва: ");
            String title = scanner.nextLine();
            System.out.print("Автор: ");
            String author = scanner.nextLine();
            System.out.print("Рік видання: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Ціна: ");
            double price = Double.parseDouble(scanner.nextLine());
            Genre selectedGenre = chooseGenre();

            // Поліморфне створення об'єктів
            if (type == 1) {
                books.add(new Book(title, author, year, price, selectedGenre));
            } else if (type == 2) {
                System.out.print("Розмір файлу (MB): ");
                double size = Double.parseDouble(scanner.nextLine());
                books.add(new EBook(title, author, year, price, selectedGenre, size));
            } else if (type == 3) {
                System.out.print("Вага книги (г): ");
                int weight = Integer.parseInt(scanner.nextLine());
                books.add(new PaperBook(title, author, year, price, selectedGenre, weight));
            }

            System.out.println("Книгу успішно додано!");

        } catch (NumberFormatException e) {
            System.out.println("Помилка: Рік, ціна, розмір та вага повинні бути числами!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації: " + e.getMessage());
        }
    }

    /**
     * Демонстрація поліморфізму: виклик методу toString() автоматично підтягує
     * версію методу з відповідного класу-нащадка.
     */
    private static void printAllBooks() {
        System.out.println("\n--- Повний список книг (Поліморфізм) ---");
        if (books.isEmpty()) {
            System.out.println("Список порожній.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

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
}