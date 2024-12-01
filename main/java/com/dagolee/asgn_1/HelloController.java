package com.dagolee.asgn_1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;

public class HelloController {

    public Button endButton;

    @FXML
    private Button addButton, borrowButton, returnButton, searchButton, displayButton;

    private final Library library = new Library();

    @FXML
    private void onAddButtonClick() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add Books");
        dialog.initOwner(addButton.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        // Layout for adding books
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 150));

        // Text fields setup
        TextField title = new TextField();
        title.setPromptText("Title");
        TextField author = new TextField();
        author.setPromptText("Author");
        TextField isbn = new TextField();
        isbn.setPromptText("ISBN");

        // ComboBox for book availability
        ComboBox<String> availability = new ComboBox<>();
        availability.getItems().addAll("AVAILABLE", "UNAVAILABLE");
        availability.setValue("AVAILABLE");

        TextField borrowerName = new TextField();
        borrowerName.setPromptText("Borrower Name");
        borrowerName.setText(".");
        borrowerName.setEditable(false);

        // Listen for changes in availability
        availability.setOnAction(e -> {
            if ("UNAVAILABLE".equals(availability.getValue())) {
                borrowerName.setEditable(true);
                borrowerName.setText("");
            } else {
                borrowerName.setEditable(false);
                borrowerName.setText(".");
            }
        });

        // Styling and layout for fields and labels
        String textFieldStyle = "-fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-background-color: #ffffff;";
        title.setStyle(textFieldStyle);
        author.setStyle(textFieldStyle);
        isbn.setStyle(textFieldStyle);
        availability.setStyle(textFieldStyle);
        borrowerName.setStyle(textFieldStyle);

        String labelStyle = "-fx-font-size: 14px; -fx-text-fill: #333;";
        grid.addRow(0, new Label("Title:"), title);
        grid.addRow(1, new Label("Author:"), author);
        grid.addRow(2, new Label("ISBN:"), isbn);
        grid.addRow(3, new Label("Availability:"), availability);
        grid.addRow(4, new Label("Borrower Name:"), borrowerName);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-background-radius: 15; -fx-padding: 10 20 10 20; -fx-cursor: hand;");
        grid.add(submitButton, 1, 5);
        GridPane.setMargin(submitButton, new Insets(20, 0, 0, 0));

        submitButton.setOnAction(e -> {
            if (!title.getText().isEmpty() && !author.getText().isEmpty() && !isbn.getText().isEmpty() && !borrowerName.getText().isEmpty()) {
                if(isbn.getText().matches("\\d{3}-\\d{1}-\\d{5}-\\d{3}-\\d{1}") || isbn.getText().matches("\\d{3}-\\d{1}-\\d{5}-\\d{1}"))
                {
                    library.addBook(title.getText(), author.getText(), isbn.getText(), availability.getValue(), borrowerName.getText().equals("-") ? null : borrowerName.getText());
                    library.saveToFile();
                    dialog.close();

                    // Confirmation alert for adding a book
                    Alert addAlert = new Alert(Alert.AlertType.INFORMATION);
                    addAlert.setTitle("Add Book");
                    addAlert.setContentText("Book '" + title.getText() + "' successfully added!");
                    addAlert.setHeaderText(null);
                    addAlert.setGraphic(null);
                    addAlert.showAndWait();
                } else {
                    showIsbnErrorAlert();
                }
            } else {
                showErrorAlert();
            }
        });

        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("All fields must be filled!");
        alert.showAndWait();
    }

    private void showIsbnErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("Invalid ISBN format!");
        alert.showAndWait();
    }

    @FXML
    protected void onBorrowButtonClick() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Borrow Books");
        dialog.initOwner(borrowButton.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField searchField = new TextField();
        searchField.setPromptText("Enter book title to search");

        Button searchButton = new Button("Search");
        TableView<Book> table = new TableView<>();
        configureBorrowTableColumns(table);

        searchButton.setOnAction(_ -> {
            String query = searchField.getText();
            ArrayList<Book> results = library.searchBookByTitle(query);
            table.setItems(FXCollections.observableArrayList(results));
        });

        layout.getChildren().addAll(new Label("Search by Title:"), searchField, searchButton, table);
        dialog.getDialogPane().setContent(layout);
        dialog.showAndWait();
    }

    // Layout of the table
    private void configureBorrowTableColumns(TableView<Book> table) {
        // Column Setup with styling matching the Add Books section
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(300);
        titleCol.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(200);
        authorCol.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        TableColumn<Book, String> availabilityCol = new TableColumn<>("Availability");
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        availabilityCol.setPrefWidth(100);
        availabilityCol.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        TableColumn<Book, Void> actionCol = getBookVoidTableColumn();

        table.getColumns().addAll(titleCol, authorCol, availabilityCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Book, Void> getBookVoidTableColumn() {
        TableColumn<Book, Void> actionCol = new TableColumn<>("Action");
        actionCol.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        actionCol.setCellFactory(_ -> new TableCell<>() {
            private final Button borrowButton = new Button("Borrow");

            {
                borrowButton.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-padding: 5; -fx-cursor: hand;");
                borrowButton.setMinWidth(90);
                borrowButton.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    borrowButton.setOnAction(_ -> {
                        if ("AVAILABLE".equals(book.getAvailability())) {
                            showBorrowDialog(book);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "This book is currently unavailable.");
                            alert.setHeaderText(null);
                            alert.setGraphic(null);
                            alert.showAndWait();
                        }
                    });
                    setGraphic(borrowButton);
                }
            }
        });

        actionCol.setMinWidth(100);
        actionCol.setPrefWidth(100);
        actionCol.setMaxWidth(100);
        return actionCol;
    }

    private void showBorrowDialog(Book book) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Borrower's Name");
        dialog.setHeaderText("Borrowing '" + book.getTitle() + "'");
        dialog.setContentText("Enter borrower's name:");
        dialog.getDialogPane().setGraphic(null);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                if (book.borrowBook(name)) { // Check if the book is successfully borrowed
                    library.saveToFile();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Book borrowed successfully.");
                    successAlert.setHeaderText(null);
                    successAlert.setGraphic(null);
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "The book could not be borrowed. It may already be unavailable.");
                    errorAlert.setHeaderText(null);
                    errorAlert.setGraphic(null);
                    errorAlert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Borrower name cannot be empty.");
                alert.setGraphic(null);
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void onSearchButtonClick() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Search Books");
        dialog.initOwner(searchButton.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Search fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> criteriaBox = new ComboBox<>();
        criteriaBox.getItems().addAll("Title", "Author", "ISBN");
        criteriaBox.setValue("Title");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search");

        Button searchButton = new Button("Search");

        grid.add(new Label("Search by:"), 0, 0);
        grid.add(criteriaBox, 1, 0);
        grid.add(new Label("Search:"), 0, 1);
        grid.add(searchField, 1, 1);
        grid.add(searchButton, 1, 2);

        // TableView setup
        TableView<Book> table = new TableView<>();
        configureTableColumns(table);

        layout.getChildren().addAll(grid, table);
        dialog.getDialogPane().setContent(layout);

        // Set the search button action
        searchButton.setOnAction(_ -> {
            String criteria = criteriaBox.getValue();
            String query = searchField.getText();
            ArrayList<Book> results = library.searchBook(criteria, query);
            table.setItems(FXCollections.observableArrayList(results));
        });

        dialog.showAndWait();
    }



    private void configureTableColumns(TableView<Book> table) {
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(200);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        isbnCol.setPrefWidth(180);

        TableColumn<Book, String> availabilityCol = new TableColumn<>("Availability");
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        availabilityCol.setPrefWidth(100);

        TableColumn<Book, String> borrowerNameCol = new TableColumn<>("Borrower Name");
        borrowerNameCol.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));
        borrowerNameCol.setPrefWidth(200);

        table.getColumns().addAll(titleCol, authorCol, isbnCol, availabilityCol, borrowerNameCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    protected void onDisplayButtonClick() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Display Books");
        dialog.initOwner(displayButton.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        // Create TableView to display books
        TableView<Book> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(library.getBookList()));

        // Define columns
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));

        TableColumn<Book, String> availabilityCol = new TableColumn<>("Availability");
        availabilityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAvailability()));

        TableColumn<Book, String> borrowerNameCol = new TableColumn<>("Borrower Name");
        borrowerNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBorrowerName()));

        // Add columns to table
        table.getColumns().add(titleCol);
        table.getColumns().add(authorCol);
        table.getColumns().add(isbnCol);
        table.getColumns().add(availabilityCol);
        table.getColumns().add(borrowerNameCol);

        titleCol.setPrefWidth(200);
        authorCol.setPrefWidth(200);
        isbnCol.setPrefWidth(180);
        availabilityCol.setPrefWidth(100);
        borrowerNameCol.setPrefWidth(200);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        dialog.getDialogPane().setContent(table);
        dialog.showAndWait();
    }

    @FXML
    protected void onReturnButtonClick() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Return Books");
        dialog.initOwner(returnButton.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField searchField = new TextField();
        searchField.setPromptText("Enter book title or ISBN to search");

        Button searchButton = new Button("Search");
        TableView<Book> table = new TableView<>();
        configureReturnTableColumns(table);

        searchButton.setOnAction(_ -> {
            String query = searchField.getText();
            ArrayList<Book> results = library.searchBorrowedBooks(query);
            table.setItems(FXCollections.observableArrayList(results));
        });

        layout.getChildren().addAll(new Label("Search by Title or ISBN:"), searchField, searchButton, table);
        dialog.getDialogPane().setContent(layout);
        dialog.showAndWait();
    }

    private void configureReturnTableColumns(TableView<Book> table) {
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(300);
        titleCol.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        isbnCol.setPrefWidth(200);
        isbnCol.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        TableColumn<Book, Void> returnCol = new TableColumn<>("Return");
        returnCol.setPrefWidth(100);
        returnCol.setCellFactory(_ -> new TableCell<>() {
            private final Button returnButton = new Button("Return");

            {
                returnButton.setStyle("-fx-background-color: #FF6F61; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 5; -fx-cursor: hand;");
                returnButton.setMinWidth(90);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    returnButton.setOnAction(_ -> {
                        if (!"AVAILABLE".equals(book.getAvailability())) {
                            if (book.returnBook()) {
                                library.saveToFile();
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book returned successfully.");
                                alert.setHeaderText(null);
                                alert.setGraphic(null);
                                alert.showAndWait();
                                refreshTable(table);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to return the book.");
                                alert.setHeaderText(null);
                                alert.setGraphic(null);
                                alert.showAndWait();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "This book has already been returned.");
                            alert.setHeaderText(null);
                            alert.setGraphic(null);
                            alert.showAndWait();
                        }
                    });
                    setGraphic(returnButton);
                }
            }
        });

        table.getColumns().addAll(titleCol, isbnCol, returnCol);
    }

    // Refresh the table
    private void refreshTable(TableView<Book> table) {
        table.refresh();
    }

    // Exit the program
    @FXML
    protected void onEndButtonClick() {
        System.exit(0);
    }
}