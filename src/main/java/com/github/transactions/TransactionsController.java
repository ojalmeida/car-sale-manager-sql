package com.github.transactions;

import com.github.entities.Car;
import com.github.entities.Transaction;
import com.github.garage.newCarController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.github.main.Main;
import com.github.services.DataStorageService;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {

      public TableView<Transaction> tableView;
      public Button exitButton;
      public Button newSaleButton;
      public Button removeButton;
      public Button backButton;
      public TextField searchTextField;
      public TableColumn<Transaction, String> brand;
      public TableColumn<Transaction, String> model;
      public TableColumn<Transaction, String> value;
      public TableColumn<Transaction, String> date;
      public TableColumn<Transaction, String> salesman;



      public void onActionExitButton(ActionEvent actionEvent) {

            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
      }
      public void onActionNewTransactionButton(ActionEvent actionEvent) throws IOException {

            Stage popup = new Stage();
            popup.setScene(new Scene(FXMLLoader.load(Main.class.getResource("/fxml/newTransaction.fxml"))));
            popup.initStyle(StageStyle.UNDECORATED);
            popup.setX(380);
            popup.setY(260);
            popup.show();

            new UpdateService(tableView).start();


      }
      public void onActionRemoveButton(ActionEvent actionEvent) throws IOException {
            Transaction transaction = tableView.getSelectionModel().getSelectedItem();
            if(transaction != null){

                  DataStorageService.removeTransaction(transaction);
                  tableView.getItems().remove(transaction);

            }
      }
      public void onActionBackButton(ActionEvent actionEvent) throws IOException {
            Main.enterMain((Stage) exitButton.getScene().getWindow());
      }

      public void updateTable(){
            try {
                  tableView.setItems(DataStorageService.transactions());

            } catch (IOException | ParseException e) {
                  e.printStackTrace();
            }
      }


      @Override
      public void initialize(URL url, ResourceBundle resourceBundle) {


            updateTable();

            date.setCellValueFactory(new PropertyValueFactory<>("Date"));
            date.setCellValueFactory(entry -> new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(entry.getValue().getDate())));

            salesman.setCellValueFactory(new PropertyValueFactory<>("Salesman"));
            salesman.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getSalesman().getUsername()));

            brand.setCellValueFactory(new PropertyValueFactory<>("Brand"));
            brand.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getCarBrand()));

            model.setCellValueFactory(new PropertyValueFactory<>("Model"));
            model.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getCarModel()));

            value.setCellValueFactory(new PropertyValueFactory<>("Value"));
            value.setCellValueFactory(entry -> new SimpleStringProperty(Double.toString(entry.getValue().getCarValue())));
            value.setCellFactory(column -> new TableCell<>() {
                  @Override
                  protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(empty ? "" : getItem());
                        setGraphic(null);

                        TableRow<Transaction> currentRow = getTableRow();

                        tableView.refresh();
                        if (!isEmpty()) {

                              if (Double.parseDouble(item) < 0) {
                                    currentRow.setStyle("-fx-background-color:#ff6666");
                              }

                              else{
                                    currentRow.setStyle("-fx-background-color:#66ff66");
                              }
                        }
                  }
            });

            searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {

                  tableView.getItems().setAll(DataStorageService.searchTransactions(newValue));
            });

      }

      public static class UpdateService extends Service<Void> {

            private UpdateService(TableView<Transaction> tableView){
                  setOnSucceeded(workerStateEvent -> {
                        try {
                              tableView.getItems().setAll(DataStorageService.transactions());
                        } catch (IOException | ParseException e) {
                              e.printStackTrace();
                        }
                  });
            }

            @Override
            protected Task<Void> createTask() {
                  return new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                              while(!newTransactionController.finished){
                                    Thread.sleep(10);
                              }
                              return null;
                        }
                  };
            }
      }
}
