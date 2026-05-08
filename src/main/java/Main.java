import com.google.gson.*;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator;

/**
 * Драйвер програми для керування бібліотекою.
 * Оновлено: додано модифікацію та видалення об'єктів.
 */
public class Main {
    /** Клас-контейнер для агрегації об'єктів (замість прямого ArrayList). */
    private static final Library library = new Library();
    /** Сканер для зчитування вводу користувача. */
    private static final Scanner scanner = new Scanner(System.in);

    private static final String FILE_NAME = "input.txt";
    private static final String JSON_FILE_NAME = "input.json";

    // Прапорець для вибору режиму (TXT або JSON)
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
            System.out.println("3. Модифікувати книгу (за UUID)");
            System.out.println("4. Видалити книгу (за UUID)");
            System.out.println("5. Пошук об’єкта");
            System.out.println("6. Вивести відсортовану інформацію");
            System.out.println("7. Завершити роботу програми");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> objectCreationMenu();
                case "2" -> printAllBooks();
                case "3" -> updateBookMenu();
                case "4" -> deleteBookMenu();
                case "5" -> searchMenu();
                case "6" -> printSortedBooks();
                case "7" -> {
                    handleExit();
                    running = false;
                }
                default -> System.out.println("Помилка: Оберіть пункт від 1 до 7.");
            }
        }
    }

    /**
     * Метод для модифікації атрибутів існуючої книги.
     */
    private static void updateBookMenu() {
        if (library.getItems().isEmpty()) {
            System.out.println("Бібліотека порожня. Нічого модифікувати.");
            return;
        }

        System.out.print("Введіть UUID книги для модифікації: ");
        String uuidStr = scanner.nextLine();
        LibraryItem item = library.searchByUuid(uuidStr);

        if (item != null) {
            System.out.println("Знайдено об'єкт: " + item);
            try {
                System.out.println("\n--- Введіть нові значення (або натисніть Enter, щоб залишити старі) ---");
                Book b = item.getBook();

                System.out.print("Нова назва [" + b.getTitle() + "]: ");
                String title = scanner.nextLine();
                if (!title.isEmpty()) b.setTitle(title);

                System.out.print("Новий автор [" + b.getAuthor() + "]: ");
                String author = scanner.nextLine();
                if (!author.isEmpty()) b.setAuthor(author);

                System.out.print("Новий рік [" + b.getYear() + "]: ");
                String yearStr = scanner.nextLine();
                if (!yearStr.isEmpty()) b.setYear(Integer.parseInt(yearStr));

                System.out.print("Нова ціна [" + b.getPrice() + "]: ");
                String priceStr = scanner.nextLine();
                if (!priceStr.isEmpty()) b.setPrice(Double.parseDouble(priceStr));

                System.out.println("Бажаєте змінити жанр? (y/n)");
                if (scanner.nextLine().equalsIgnoreCase("y")) {
                    b.setGenre(chooseGenre());
                }

                System.out.println("Результат: Дані успішно оновлено через сетери класу.");
            } catch (Exception e) {
                System.out.println("Помилка при оновленні: " + e.getMessage());
            }
        } else {
            System.out.println("Об'єкт з таким UUID не знайдено.");
        }
    }

    /**
     * Метод для видалення книги з колекції.
     */
    private static void deleteBookMenu() {
        if (library.getItems().isEmpty()) {
            System.out.println("Бібліотека порожня. Нічого видаляти.");
            return;
        }

        System.out.print("Введіть UUID книги для видалення: ");
        String uuidStr = scanner.nextLine();
        LibraryItem item = library.searchByUuid(uuidStr);

        if (item != null) {
            System.out.println("Буде видалено: " + item);
            System.out.print("Ви впевнені? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                boolean result = library.delete(item);
                if (result) {
                    System.out.println("Об'єкт успішно видалено з колекції.");
                } else {
                    System.out.println("Помилка під час видалення.");
                }
            } else {
                System.out.println("Видалення скасовано.");
            }
        } else {
            System.out.println("Об'єкт не знайдено.");
        }
    }

    // --- Метод фільтрації ---
    /**
     * Виводить відсортований список книг.
     */
    private static void printSortedBooks() {
        System.out.println("\n--- ОБЕРІТЬ КРИТЕРІЙ СОРТУВАННЯ ---");
        System.out.println("1. За назвою (алфавіт)");
        System.out.println("2. За роком видання (від нових до старих)");
        System.out.println("3. За ціною (від дешевих до дорогих)");
        System.out.println("0. Повернутися в головне меню");
        System.out.print("Ваш вибір: ");

        String choice = scanner.nextLine();
        Comparator<LibraryItem> comparator = null;

        switch (choice) {
            case "1":
                // Лямбда-вираз для сортування за назвою
                comparator = (o1, o2) -> o1.getBook().getTitle().compareToIgnoreCase(o2.getBook().getTitle());
                break;
            case "2":
                // Лямбда-вираз для сортування за роком (спадання)
                comparator = (o1, o2) -> Integer.compare(o2.getBook().getYear(), o1.getBook().getYear());
                break;
            case "3":
                // Лямбда-вираз для сортування за ціною
                comparator = (o1, o2) -> Double.compare(o1.getBook().getPrice(), o2.getBook().getPrice());
                break;
            case "0":
                return;
            default:
                System.out.println("Невірний вибір.");
                return;
        }

        List<LibraryItem> results = library.getSortedItems(comparator);
        displaySortedResults(results);
    }

    private static void displaySortedResults(List<LibraryItem> results) {
        if (results.isEmpty()) {
            System.out.println("Бібліотека порожня.");
        } else {
            System.out.println("\n--- ВІДСОРТОВАНИЙ РЕЗУЛЬТАТ ---");
            for (LibraryItem item : results) {
                System.out.println(item);
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
        System.out.println("4. Пошук за унікальним ID (UUID)");
        System.out.println("0. Повернутися до головного меню");
        System.out.print("Ваш вибір: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> searchByAuthor();
            case "2" -> searchByYear();
            case "3" -> searchByMaxPrice();
            case "4" -> searchByUuid();
            case "0" -> { /* Повернення в меню */ }
            default -> System.out.println("Невірний вибір.");
        }
    }

    /**
     * Виконує пошук за повним UUID через клас Library.
     */
    private static void searchByUuid() {
        System.out.print("Введіть повний UUID об'єкта: ");
        String uuidStr = scanner.nextLine();
        LibraryItem item = library.searchByUuid(uuidStr);

        System.out.println("\n--- Результат пошуку за UUID ---");
        if (item != null) {
            System.out.println("Знайдено: " + item);
        } else {
            System.out.println("Об'єкт з таким ID не знайдено.");
        }
    }

    /**
     * Виконує пошук за автором через клас Library.
     */
    private static void searchByAuthor() {
        System.out.print("Введіть ім'я автора: ");
        String author = scanner.nextLine();
        displaySearchResults(library.searchByAuthor(author));
    }

    /**
     * Виконує пошук за роком видання через клас Library.
     */
    private static void searchByYear() {
        try {
            System.out.print("Введіть мінімальний рік видання: ");
            int year = Integer.parseInt(scanner.nextLine());
            displaySearchResults(library.searchByYear(year));
        } catch (NumberFormatException e) {
            System.out.println("Помилка: Рік має бути числом.");
        }
    }

    /**
     * Виконує пошук за ціною через клас Library.
     */
    private static void searchByMaxPrice() {
        try {
            System.out.print("Введіть максимальну ціну: ");
            double maxPrice = Double.parseDouble(scanner.nextLine());
            displaySearchResults(library.searchByMaxPrice(maxPrice));
        } catch (NumberFormatException e) {
            System.out.println("Помилка: Ціна має бути числом.");
        }
    }

    /**
     * Універсальний метод для виведення результатів пошуку.
     */
    private static void displaySearchResults(List<LibraryItem> results) {
        System.out.println("\n--- Результати пошуку ---");
        if (results.isEmpty()) {
            System.out.println("Нічого не знайдено за вашим запитом.");
        } else {
            for (LibraryItem item : results) {
                System.out.println(item);
            }
            System.out.println("Знайдено позицій: " + results.size());
        }
    }

    // --- Методи Load/Save ---
    /**
     * Запитує користувача куди зберегти дані перед закриттям програми.
     */
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
     * Читання даних з формату JSON та додавання до контейнера Library.
     */
    private static void loadFromJson() {
        File file = new File(JSON_FILE_NAME);
        if (!file.exists()) return;
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(JSON_FILE_NAME)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String type = jsonObject.get("type").getAsString();
                int quantity = jsonObject.get("quantity").getAsInt();

                Book book = switch (type) {
                    case "EBook" -> gson.fromJson(jsonObject, EBook.class);
                    case "PaperBook" -> gson.fromJson(jsonObject, PaperBook.class);
                    case "AudioBook" -> gson.fromJson(jsonObject, AudioBook.class);
                    case "RareBook" -> gson.fromJson(jsonObject, RareBook.class);
                    default -> null;
                };

                if (book != null) {
                    library.addNewBook(book, quantity);
                }
            }
            System.out.println("Дані завантажено з JSON.");
        } catch (Exception e) {
            System.out.println("Помилка завантаження JSON: " + e.getMessage());
        }
    }

    /**
     * Збереження даних з контейнера Library у форматі JSON.
     */
    private static void saveToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(JSON_FILE_NAME)) {
            JsonArray jsonArray = new JsonArray();
            for (LibraryItem item : library.getItems()) {
                // Перетворюємо книгу в JSON вузол
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("type", item.getBook().getClass().getSimpleName());
                itemObject.addProperty("quantity", item.getQuantity());

                // Додаємо всі поля книги
                JsonObject bookData = gson.toJsonTree(item.getBook()).getAsJsonObject();
                for (String key : bookData.keySet()) {
                    itemObject.add(key, bookData.get(key));
                }

                jsonArray.add(itemObject);
            }
            gson.toJson(jsonArray, writer);
            System.out.println("Дані збережено у JSON.");
        } catch (IOException e) {
            System.out.println("Помилка запису JSON: " + e.getMessage());
        }
    }

    /**
     * Зчитує дані з файлу input.txt та додає їх у бібліотеку через метод addNewBook.
     */
    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|");

                // Визначаємо індекс останнього елемента (кількість)
                int lastIndex = p.length - 1;
                int quantity = Integer.parseInt(p[lastIndex]);

                Book bk = switch (p[0]) {
                    case "EBook" -> new EBook(p[1], p[2], Integer.parseInt(p[3]), Double.parseDouble(p[4]), Genre.valueOf(p[5]), Double.parseDouble(p[6]));
                    case "PaperBook" -> new PaperBook(p[1], p[2], Integer.parseInt(p[3]), Double.parseDouble(p[4]), Genre.valueOf(p[5]), Integer.parseInt(p[6]));
                    case "AudioBook" -> new AudioBook(p[1], p[2], Integer.parseInt(p[3]), Double.parseDouble(p[4]), Genre.valueOf(p[5]), Integer.parseInt(p[6]));
                    case "RareBook" -> new RareBook(p[1], p[2], Integer.parseInt(p[3]), Double.parseDouble(p[4]), Genre.valueOf(p[5]), p[6]);
                    default -> null;
                };

                if (bk != null) {
                    library.addNewBook(bk, quantity);
                }
            }
            System.out.println("Дані завантажено з TXT.");
        } catch (Exception e) {
            System.out.println("Помилка при читанні TXT: " + e.getMessage());
        }
    }

    /**
     * Записує актуальний вміст інвентарю бібліотеки у файл input.txt.
     */
    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (LibraryItem item : library.getItems()) {
                Book b = item.getBook();
                StringBuilder sb = new StringBuilder();
                // Початкові поля
                sb.append(b.getClass().getSimpleName()).append("|")
                        .append(b.getTitle()).append("|")
                        .append(b.getAuthor()).append("|")
                        .append(b.getYear()).append("|")
                        .append(b.getPrice()).append("|")
                        .append(b.getGenre().name());

                // Специфічні поля для підтипів
                if (b instanceof EBook) sb.append("|").append(((EBook) b).getFileSizeMb());
                else if (b instanceof PaperBook) sb.append("|").append(((PaperBook) b).getWeightGrams());
                else if (b instanceof AudioBook) sb.append("|").append(((AudioBook) b).getDurationMinutes());
                else if (b instanceof RareBook) sb.append("|").append(((RareBook) b).getCondition());

                // Кількість як останній елемент
                sb.append("|").append(item.getQuantity());

                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.out.println("Помилка при збереженні у файл: " + e.getMessage());
        }
    }

    // --- Методи створення об'єктів ---
    /**
     * Підменю для вибору конкретного типу об'єкта, який необхідно створити.
     */
    private static void objectCreationMenu() {
        System.out.println("\n--- Оберіть тип нового об'єкта ---");
        // Пункт "Звичайна книга (Book)" видалено, бо клас абстрактний
        System.out.println("1. Електронна книга (EBook)");
        System.out.println("2. Паперова книга (PaperBook)");
        System.out.println("3. Аудіокнига (AudioBook)");
        System.out.println("4. Рідкісна книга (RareBook)");
        System.out.println("0. Повернутися до головного меню");
        System.out.print("Ваш вибір: ");

        String choice = scanner.nextLine();
        if (choice.equals("0")) return;

        try {
            int type = Integer.parseInt(choice);
            if (type >= 1 && type <= 4) {
                addBook(type + 1);
            } else {
                System.out.println("Помилка: Оберіть тип від 1 до 4.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка: Введіть число.");
        }
    }

    /**
     * Універсальний метод для введення даних та створення об'єктів.
     * @param type тип об'єкта (1-Book, 2-EBook, 3-PaperBook, 4-AudioBook, 5-RareBook)
     */
    private static void addBook(int type) {
        try {
            System.out.println("\n--- Введення загальних даних ---");
            System.out.print("Назва: "); String title = scanner.nextLine();
            System.out.print("Автор: "); String author = scanner.nextLine();
            System.out.print("Рік видання: "); int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Ціна: "); double price = Double.parseDouble(scanner.nextLine());
            Genre selectedGenre = chooseGenre();

            Book bk = switch (type) {
                case 2 -> {
                    System.out.print("Розмір файлу (MB): ");
                    yield new EBook(title, author, year, price, selectedGenre, Double.parseDouble(scanner.nextLine()));
                }
                case 3 -> {
                    System.out.print("Вага книги (г): ");
                    yield new PaperBook(title, author, year, price, selectedGenre, Integer.parseInt(scanner.nextLine()));
                }
                case 4 -> {
                    System.out.print("Тривалість (хв): ");
                    yield new AudioBook(title, author, year, price, selectedGenre, Integer.parseInt(scanner.nextLine()));
                }
                case 5 -> {
                    System.out.print("Стан (напр. Нова, Пошкоджена): ");
                    yield new RareBook(title, author, year, price, selectedGenre, scanner.nextLine());
                }
                default -> null;
            };

            if (bk != null) {
                System.out.print("Кількість копій: ");
                int q = Integer.parseInt(scanner.nextLine());
                library.addNewBook(bk, q);
                System.out.println("Об'єкт успішно додано до бібліотеки!");
            }

        } catch (Exception e) {
            System.out.println("Помилка: Введіть коректні значення!");
        }
    }

    /**
     * Виводить інформацію про весь інвентар бібліотеки (об'єкти та їх кількість).
     */
    private static void printAllBooks() {
        System.out.println("\n--- Інвентар бібліотеки (Агрегація) ---");
        List<LibraryItem> items = library.getItems();
        if (items.isEmpty()) {
            System.out.println("Бібліотека порожня.");
        } else {
            for (LibraryItem item : items) {
                System.out.println(item);
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