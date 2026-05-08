import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Library library = new Library();
    private ListView<String> listView = new ListView<>();
    private TextArea detailsArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система управління бібліотекою (UUID)");

        // --- Блок 1: Додавання об'єкта ---
        VBox addBox = new VBox(10);
        addBox.setPadding(new Insets(10));
        addBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");

        TextField titleField = new TextField(); titleField.setPromptText("Назва книги");
        TextField authorField = new TextField(); authorField.setPromptText("Автор");
        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList(
                "PaperBook", "EBook", "AudioBook", "RareBook"
        ));
        typeBox.setValue("PaperBook");
        Button addBtn = new Button("Додати в колекцію");

        addBtn.setOnAction(e -> {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String type = typeBox.getValue();

                // Створюємо об'єкт (зі значеннями за замовчуванням)
                Book newBook = switch (type) {
                    case "EBook" -> new EBook(title, author, 2026, 100, Genre.TECHNICAL, 10.5);
                    case "AudioBook" -> new AudioBook(title, author, 2026, 150, Genre.FICTION, 120);
                    case "RareBook" -> new RareBook(title, author, 1900, 5000, Genre.HISTORY, "Excellent");
                    default -> new PaperBook(title, author, 2026, 200, Genre.FICTION, 350);
                };

                library.addNewBook(newBook, 1);
                updateListView();
                titleField.clear(); authorField.clear();
            } catch (Exception ex) {
                showError("Помилка додавання: " + ex.getMessage());
            }
        });
        addBox.getChildren().addAll(new Label("Нова книга:"), typeBox, titleField, authorField, addBtn);

        // --- Блок 2: Пошук за UUID ---
        HBox searchBox = new HBox(10);
        TextField uuidSearchField = new TextField();
        uuidSearchField.setPromptText("Вставте UUID для пошуку");
        uuidSearchField.setPrefWidth(300);
        Button searchBtn = new Button("Знайти");

        searchBtn.setOnAction(e -> {
            String input = uuidSearchField.getText().trim();

            try {
                // Метод кидає виняток, якщо об'єкт не знайдено
                LibraryItem found = library.searchByUuid(input);

                // Якщо виняток не вилетів — значить знайшли
                detailsArea.setText("РЕЗУЛЬТАТ ПОШУКУ:\n" + found.getBook().toString());

            } catch (BookNotFoundException ex) {
                detailsArea.setText("ПОМИЛКА: " + ex.getMessage());
            }
        });
        searchBox.getChildren().addAll(uuidSearchField, searchBtn);
        // Витягуємо UUID з рядка
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String uuid = newValue.substring(newValue.lastIndexOf("UUID: ") + 6);
                uuidSearchField.setText(uuid);
            }
        });

        // --- Блок 3: Вивід списку та деталей ---
        VBox listVBox = new VBox(5);
        detailsArea.setEditable(false);
        detailsArea.setPrefHeight(100);
        listVBox.getChildren().addAll(new Label("Список книг (UUID):"), listView, new Label("Деталі об'єкта:"), detailsArea);

        // Головне компонування
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(15));
        mainLayout.getChildren().addAll(addBox, searchBox, listVBox);

        primaryStage.setScene(new Scene(mainLayout, 600, 700));
        primaryStage.show();
    }

    private void updateListView() {
        listView.getItems().clear();
        for (LibraryItem item : library.getItems()) {
            listView.getItems().add(item.getBook().toShortString());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}