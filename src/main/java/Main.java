import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Book[] books = new Book[5];

        System.out.println("Введення даних для 5 книг:");

        for (int i = 0; i < books.length; i++) {
            System.out.println("\nКнига №" + (i + 1));
            System.out.print("Назва: ");
            String title = scanner.nextLine();

            System.out.print("Автор: ");
            String author = scanner.nextLine();

            System.out.print("Рік видання: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Ціна: ");
            double price = Double.parseDouble(scanner.nextLine());

            books[i] = new Book(title, author, year, price);
        }

        System.out.println("\n--- Список ваших книг ---");
        for (Book b : books) {
            System.out.println(b.toString());
        }

        scanner.close();
    }
}
