//This program is my final project. It is a
//BudgetTracker GUI that tracks income and
//expense inputted from the user. The user
//will type in their income and expense
//amounts in the appropriate fields and
//click the submit button to submit them
//to the tables on the right. Above the
//submit button will be details of the
//amounts of income and expense inputted
//so far as well as a balance to show how
//much the user has left to spend.

package com.example.secondbudgettrackergui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IncomeExpenseSummary extends Application {

    private TextField incomeField;
    private TextField expenseField;
    private Label summaryLabel;

    // Declare the lists for the income and expense data
    private ObservableList<IncomeData> incomeList;
    private ObservableList<ExpenseData> expenseList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Income and Expense Summary");

        // Create the income field
        Label incomeLabel = new Label("Income:");
        incomeField = new TextField();
        GridPane.setConstraints(incomeLabel, 0, 0);
        GridPane.setConstraints(incomeField, 1, 0);

        // Create the expense field
        Label expenseLabel = new Label("Expense:");
        expenseField = new TextField();
        GridPane.setConstraints(expenseLabel, 0, 1);
        GridPane.setConstraints(expenseField, 1, 1);

        // Create the summary label
        summaryLabel = new Label();
        VBox summaryBox = new VBox(summaryLabel);
        summaryBox.setPadding(new Insets(10, 0, 0, 0));
        GridPane.setConstraints(summaryBox, 0, 2, 2, 1);

        // Create the grid pane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.getChildren().addAll(incomeLabel, incomeField, expenseLabel, expenseField, summaryBox);

        // Initialize the lists for the income and expense data
        incomeList = FXCollections.observableArrayList();
        expenseList = FXCollections.observableArrayList();

        // Create the income table
        TableView<IncomeData> incomeTable = new TableView<>();
        // Create the columns for the income table
        TableColumn<IncomeData, String> incomeSourceColumn = new TableColumn<>("Source");
        incomeSourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
        TableColumn<IncomeData, Double> incomeAmountColumn = new TableColumn<>("Amount");
        incomeAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        // Add the columns to the income table
        incomeTable.getColumns().add(incomeSourceColumn);
        incomeTable.getColumns().add(incomeAmountColumn);
        // Bind the income table's items to the income list
        incomeTable.setItems(incomeList);
        // Add the income table to the grid pane
        GridPane.setConstraints(incomeTable, 2, 0, 1, 2);
        grid.getChildren().add(incomeTable);

        // Create the expense table
        TableView<ExpenseData> expenseTable = new TableView<>();
        // Create the columns for the expense table
        TableColumn<ExpenseData, String> expenseCategoryColumn = new TableColumn<>("Category");
        expenseCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<ExpenseData, Double> expenseAmountColumn = new TableColumn<>("Amount");
        expenseAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        // Add the columns to the expense table
        expenseTable.getColumns().add(expenseCategoryColumn);
        expenseTable.getColumns().add(expenseAmountColumn);
        // Bind the expense table's items to the expense list
        expenseTable.setItems(expenseList);
        // Add the expense table to the grid pane
        GridPane.setConstraints(expenseTable, 3, 0, 1, 2);
        grid.getChildren().add(expenseTable);

        // Create the submit button
        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 3);
        grid.getChildren().add(submitButton);

        // Set the action handler for the submit button
        submitButton.setOnAction(e -> {
            // Get the income and expense values from the fields
            double income = 0;
            double expense = 0;
            try {
                income = Double.parseDouble(incomeField.getText());
            } catch (NumberFormatException ex) {
                // Ignore
            }
            try {
                expense = Double.parseDouble(expenseField.getText());
            } catch (NumberFormatException ex) {
                // Ignore
            }
            // Create an IncomeData object and an ExpenseData object
            IncomeData incomeData = new IncomeData("Income", income);
            ExpenseData expenseData = new ExpenseData("Expense", expense);
            // Add the objects to the income and expense lists
            incomeList.add(incomeData);
            expenseList.add(expenseData);
            // Clear the income and expense fields
            incomeField.setText("");
            expenseField.setText("");
            // Update the summary label
            updateSummary();
        });

        // Create the scene
        Scene scene = new Scene(grid, 600, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Update the summary label
        updateSummary();
        incomeField.textProperty().addListener((observable, oldValue, newValue) -> updateSummary());
        expenseField.textProperty().addListener((observable, oldValue, newValue) -> updateSummary());
    }

    public void updateSummary() {
        double income = 0;
        double expense = 0;
        try {
            income = Double.parseDouble(incomeField.getText());
        } catch (NumberFormatException e) {
            // Ignore
        }
        try {
            expense = Double.parseDouble(expenseField.getText());
        } catch (NumberFormatException e) {
            // Ignore
        }
        // Calculate the total income and expense from the lists
        double incomeTotal = incomeList.stream().mapToDouble(IncomeData::getAmount).sum();
        double expenseTotal = expenseList.stream().mapToDouble(ExpenseData::getAmount).sum();
        // Add the income and expense fields to the totals
        incomeTotal += income;
        expenseTotal += expense;
        // Calculate the balance
        double balance = incomeTotal - expenseTotal;
        String summary = String.format("Income: $%.2f\nExpense: $%.2f\nBalance: $%.2f", incomeTotal, expenseTotal, balance);
        summaryLabel.setText(summary);
    }

    // Add a new class for the income data
    public static class IncomeData {

        private String source; // The source of the income
        private double amount; // The amount of the income

        // Constructor
        public IncomeData(String source, double amount) {
            this.source = source;
            this.amount = amount;
        }

        // Getters and setters
        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    // Add a new class for the expense data
    public static class ExpenseData {

        private String category; // The category of the expense
        private double amount; // The amount of the expense

        // Constructor
        public ExpenseData(String category, double amount) {
            this.category = category;
            this.amount = amount;
        }

        // Getters and setters
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
