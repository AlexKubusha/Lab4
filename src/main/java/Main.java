import com.google.gson.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Драйвер програми для керування бібліотекою.
 * Оновлено: додано функціонал пошуку за критеріями.
 * Демонструє принципи успадкування та поліморфізму на прикладі класів:
 * Book, EBook, PaperBook, AudioBook та RareBook.
 */
public class Main {
    /** Внутрішня колекція для зберігання всіх типів книг. */
    private static final List<Book> books = new ArrayList<>();
    /** Сканер для зчитування вводу користувача. */
    private static final Scanner scanner = new Scanner(System.in);

    private static final String FILE_NAME = "input.txt";
    private static final String JSON_FILE_NAME = "input.json";

    // Прапорець для вибору режиму
    private static boolean useJsonMode = false;

    public static void main(String[] args) {
        System.out.println("Оберіть формат роботи з даними:");
        System.out.println("1. Текстовий файл (input.txt)");
        System.out.println("2. JSON файл (input.json)");
        System.out.print("Ваш вибір: ");

        String modeChoice = scanner.nextLine();
        if (modeChoice.equals("2")) {
            useJsonMode = true;
            loadFromJson();
        } else {
            useJsonMode = false;
            loadFromFile();
        }

        boolean running = true;
        while (running) {
            System.out.println("\n========= ГОЛОВНЕ МЕНЮ (" + (useJsonMode ? "JSON" : "TXT") + ") =========");
            System.out.println("1. Створити новий об’єкт");
            System.out.println("2. Вивести інформацію про всі об’єкти");
            System.out.println("3. Пошук об’єкта (Варіант пошуку)");
            System.out.println("4. Завершити роботу програми");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> objectCreationMenu();
                case "2" -> printAllBooks();
                case "3" -> searchMenu(); // Нове підменю пошуку
                case "4" -> {
                    handleExit();
                    running = false;
                }
                default -> System.out.println("Помилка: Оберіть пункт від 1 до 4.");
            }
        }
    }

    // --- Методи пошуку ---
    /**
     * Меню пошуку об'єктів за критеріями.
     */
    private static void searchMenu() {
        System.out.println("\n--- ПІДМЕНЮ ПОШУКУ ---");
        System.out.println("1. Пошук за автором");
        System.out.println("2. Пошук за роком видання (від)");
        System.out.println("3. Пошук за максимальною ціною");
        System.out.println("0. Повернутися до головного меню");
        System.out.print("Ваш вибір: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> searchByAuthor();
            case "2" -> searchByYear();
            case "3" -> searchByMaxPrice();
            case "0" -> { /* Просто вихід у головне меню */ }
            default -> System.out.println("Невірний вибір.");
        }
    }

    /**
     * Виконує пошук книг за ім'ям автора.
     * Користувач вводить частину імені, пошук ігнорує регістр символів.
     */
    private static void searchByAuthor() {
        System.out.print("Введіть ім'я автора: ");
        String author = scanner.nextLine().toLowerCase();
        List<Book> results = new ArrayList<>();

        for (Book b : books) {
            if (b.getAuthor().toLowerCase().contains(author)) {
                results.add(b);
            }
        }
        displaySearchResults(results);
    }

    /**
     * Виконує пошук книг, рік видання яких не менший за вказаний.
     * Обробляє помилку вводу, якщо вказано не числове значення.
     */
    private static void searchByYear() {
        try {
            System.out.print("Введіть мінімальний рік видання: ");
            int year = Integer.parseInt(scanner.nextLine());
            List<Book> results = new ArrayList<>();

            for (Book b : books) {
                if (b.getYear() >= year) {
                    results.add(b);
                }
            }
            displaySearchResults(results);
        } catch (NumberFormatException e) {
            System.out.println("Помилка: Рік має бути числом.");
        }
    }

    /**
     * Виконує пошук книг, ціна яких не перевищує вказане максимальне значення.
     * Обробляє помилку вводу, якщо вказано не числове значення.
     */
    private static void searchByMaxPrice() {
        try {
            System.out.print("Введіть максимальну ціну: ");
            double maxPrice = Double.parseDouble(scanner.nextLine());
            List<Book> results = new ArrayList<>();

            for (Book b : books) {
                if (b.getPrice() <= maxPrice) {
                    results.add(b);
                }
            }
            displaySearchResults(results);
        } catch (NumberFormatException e) {
            System.out.println("Помилка: Ціна має бути числом.");
        }
    }

    /**
     * Універсальний метод для виведення результатів пошуку.
     */
    private static void displaySearchResults(List<Book> results) {
        System.out.println("\n--- Результати пошуку ---");
        if (results.isEmpty()) {
            System.out.println("Нічого не знайдено за вашим запитом.");
        } else {
            for (Book b : results) {
                System.out.println(b);
            }
            System.out.println("Знайдено об'єктів: " + results.size());
        }
    }

    // --- Методи Load/Save ---
    private static void handleExit() {
        System.out.println("\nКуди зберегти зміни перед виходом?");
        System.out.println("1. У текстовий файл (txt)");
        System.out.println("2. У JSON файл");
        System.out.println("3. Не зберігати");
        System.out.print("Ваш вибір: ");

        String exitChoice = scanner.nextLine();
        switch (exitChoice) {
            case "1" -> {
                saveToFile();
                System.out.println("Дані збережено у TXT. Бувай!");
            }
            case "2" -> {
                saveToJson();
                System.out.println("Дані збережено у JSON. Бувай!");
            }
            default -> System.out.println("Вихід без збереження. Бувай!");
        }
    }

    /**
     * Додаткове завдання: Читання з формату JSON
     */
    private static void loadFromJson() {
        File file = new File(JSON_FILE_NAME);
        if (!file.exists()) return;
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(JSON_FILE_NAME)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            books.clear();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String type = jsonObject.get("type").getAsString();
                Book book = switch (type) {
                    case "EBook" -> gson.fromJson(jsonObject, EBook.class);
                    case "PaperBook" -> gson.fromJson(jsonObject, PaperBook.class);
                    case "AudioBook" -> gson.fromJson(jsonObject, AudioBook.class);
                    case "RareBook" -> gson.fromJson(jsonObject, RareBook.class);
                    default -> gson.fromJson(jsonObject, Book.class);
                };
                books.add(book);
            }
            System.out.println("Дані завантажено з JSON.");
        } catch (Exception e) {
            System.out.println("Помилка завантаження JSON: " + e.getMessage());
        }
    }

    /**
     * Додаткове завдання: Збереження у форматі JSON
     */
    private static void saveToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(JSON_FILE_NAME)) {
            // Створюємо масив JSON об'єктів, додаючи поле type для кожного
            JsonArray jsonArray = new JsonArray();
            for (Book book : books) {
                JsonObject jsonObject = gson.toJsonTree(book).getAsJsonObject();
                jsonObject.addProperty("type", book.getClass().getSimpleName());
                jsonArray.add(jsonObject);
            }
            gson.toJson(jsonArray, writer);
            System.out.println("Дані успішно збережено у " + JSON_FILE_NAME);
        } catch (IOException e) {
            System.out.println("Помилка запису JSON: " + e.getMessage());
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
     * Записує актуальний вміст ArrayList у файл input.txt.
     */
    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                StringBuilder sb = new StringBuilder();

                // Визначаємо тип та базові поля
                String type = b.getClass().getSimpleName();
                sb.append(type).append("|")
                        .append(b.getTitle()).append("|")
                        .append(b.getAuthor()).append("|")
                        .append(b.getYear()).append("|")
                        .append(b.getPrice()).append("|")
                        .append(b.getGenre().name());

                // Додаємо специфічні поля на основі типу
                if (b instanceof EBook) {
                    sb.append("|").append(((EBook) b).getFileSizeMb());
                } else if (b instanceof PaperBook) {
                    sb.append("|").append(((PaperBook) b).getWeightGrams());
                } else if (b instanceof AudioBook) {
                    sb.append("|").append(((AudioBook) b).getDurationMinutes());
                } else if (b instanceof RareBook) {
                    sb.append("|").append(((RareBook) b).getCondition());
                }

                writer.println(sb);
            }
        } catch (IOException e) {
            System.out.println("Помилка при збереженні у файл: " + e.getMessage());
        }
    }

    // --- Методи створення об'єктів ---
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