import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * Драйвер програми для керування бібліотекою.
 * Оновлено: додано роботу з файлом input.txt.
 * Демонструє принципи успадкування та поліморфізму на прикладі класів:
 * Book, EBook, PaperBook, AudioBook та RareBook.
 */
public class Main {
    /** Внутрішня колекція для зберігання всіх типів книг. */
    private static final List<Book> books = new ArrayList<>();
    /** Сканер для зчитування вводу користувача. */
    private static final Scanner scanner = new Scanner(System.in);

    private static final String FILE_NAME = "input.txt";

    public static void main(String[] args) {
        // Завантаження даних при запуску
        loadFromFile();

        boolean running = true;
        System.out.println("Вітаємо у системі керування бібліотекою!");

        while (running) {
            // Оновлене меню згідно з вимогами ПР №8
            System.out.println("\n========= ГОЛОВНЕ МЕНЮ =========");
            System.out.println("1. Створити новий об’єкт");
            System.out.println("2. Вивести інформацію про всі об’єкти");
            System.out.println("3. Завершити роботу програми");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> objectCreationMenu(); // Перехід до підменю створення
                case "2" -> printAllBooks();
                case "3" -> {
                    running = false;
                    System.out.println("\nДякуємо за використання! Програму завершено.");
                }
                default -> System.out.println("Помилка: Оберіть пункт меню від 1 до 3.");
            }
        }
    }

    /**
     * Зчитує дані з файлу input.txt та створює об'єкти відповідних класів.
     */
    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                String type = parts[0];
                String title = parts[1];
                String author = parts[2];
                int year = Integer.parseInt(parts[3]);
                double price = Double.parseDouble(parts[4]);
                Genre genre = Genre.valueOf(parts[5]);

                switch (type) {
                    case "Book" -> books.add(new Book(title, author, year, price, genre));
                    case "EBook" -> {
                        double size = Double.parseDouble(parts[6]);
                        books.add(new EBook(title, author, year, price, genre, size));
                    }
                    case "PaperBook" -> {
                        int weight = Integer.parseInt(parts[6]);
                        books.add(new PaperBook(title, author, year, price, genre, weight));
                    }
                    case "AudioBook" -> {
                        int duration = Integer.parseInt(parts[6]);
                        books.add(new AudioBook(title, author, year, price, genre, duration));
                    }
                    case "RareBook" -> {
                        String condition = parts[6];
                        books.add(new RareBook(title, author, year, price, genre, condition));
                    }
                }
            }
            System.out.println("Дані успішно завантажено з файлу.");
        } catch (Exception e) {
            System.out.println("Помилка при читанні файлу: " + e.getMessage());
        }
    }

    /**
     * Підменю для вибору конкретного типу об'єкта, який необхідно створити.
     * Забезпечує можливість повернення до головного меню.
     */
    private static void objectCreationMenu() {
        System.out.println("\n--- Оберіть тип нового об'єкта ---");
        System.out.println("1. Звичайна книга (Book)");
        System.out.println("2. Електронна книга (EBook)");
        System.out.println("3. Паперова книга (PaperBook)");
        System.out.println("4. Аудіокнига (AudioBook)");
        System.out.println("5. Рідкісна книга (RareBook)");
        System.out.println("0. Повернутися до головного меню");
        System.out.print("Ваш вибір: ");

        String choice = scanner.nextLine();

        // Можливість повернення до головного меню без створення об'єкта
        if (choice.equals("0")) return;

        try {
            int type = Integer.parseInt(choice);
            if (type >= 1 && type <= 5) {
                addBook(type);
            } else {
                System.out.println("Помилка: Оберіть тип від 1 до 5 або 0 для виходу.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка: Введіть число.");
        }
    }

    /**
     * Універсальний метод для введення даних та створення об'єктів.
     * Розширено для 5 класів.
     * @param type тип об'єкта (1-Book, 2-EBook, 3-PaperBook, 4-AudioBook, 5-RareBook)
     */
    private static void addBook(int type) {
        try {
            System.out.println("\n--- Введення загальних даних ---");

            System.out.print("Назва: ");
            String title = scanner.nextLine();
            System.out.print("Автор: ");
            String author = scanner.nextLine();
            System.out.print("Рік видання: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Ціна: ");
            double price = Double.parseDouble(scanner.nextLine());
            Genre selectedGenre = chooseGenre();

            // Поліморфне створення об'єктів та додавання до спільної колекції
            switch (type) {
                case 1 -> books.add(new Book(title, author, year, price, selectedGenre));
                case 2 -> {
                    System.out.print("Розмір файлу (MB): ");
                    double size = Double.parseDouble(scanner.nextLine());
                    books.add(new EBook(title, author, year, price, selectedGenre, size));
                }
                case 3 -> {
                    System.out.print("Вага книги (г): ");
                    int weight = Integer.parseInt(scanner.nextLine());
                    books.add(new PaperBook(title, author, year, price, selectedGenre, weight));
                }
                case 4 -> {
                    System.out.print("Тривалість (хв): ");
                    int duration = Integer.parseInt(scanner.nextLine());
                    books.add(new AudioBook(title, author, year, price, selectedGenre, duration));
                }
                case 5 -> {
                    System.out.print("Стан (напр. Нова, Пошкоджена): ");
                    String condition = scanner.nextLine();
                    books.add(new RareBook(title, author, year, price, selectedGenre, condition));
                }
            }

            System.out.println("Об'єкт успішно додано до ArrayList!");

        } catch (NumberFormatException e) {
            System.out.println("Помилка: Введіть коректні числові значення!");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації: " + e.getMessage());
        }
    }

    /**
     * Виводить інформацію про всі об’єкти в колекції.
     * Демонструє поліморфізм: для кожного об'єкта викликається його власна версія toString().
     */
    private static void printAllBooks() {
        System.out.println("\n--- Вміст колекції (Демонстрація поліморфізму) ---");
        if (books.isEmpty()) {
            System.out.println("Колекція порожня.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    /**
     * Запитує користувача та повертає відповідний об'єкт перерахування Genre.
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
}