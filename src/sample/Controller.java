package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

  //String pointing to the local database file
  final String dburl = "jdbc:derby:lib//books";
  private int dbnum = 1;

  @FXML
  //Label for first name to be displayed
  private Label lblFirst;

  @FXML
  //Label for last name to be displayed
  private Label lblLast;

  @FXML
  //Text box for first name to be entered
  private TextField txtFirst;

  @FXML
  //Text box for last name to be entered
  private TextField txtLast;

  @FXML
    //Method to create records in the database
  void createRecord(ActionEvent event) {
    String createQuery = String.format("INSERT INTO AUTHORS(firstName, lastName)"
        + " VALUES ('%s', '%s')", txtFirst.getText(), txtLast.getText());
    connect(createQuery);
  }

  @FXML
    //Method to delete records in the database
  void deleteRecord(ActionEvent event) {
    String deleteQuery = String.format("DELETE FROM AUTHORS WHERE FIRSTNAME = '%s' AND "
        + "LASTNAME = '%s'", lblFirst.getText(), lblLast.getText());
    connect(deleteQuery);
  }

  @FXML
    //Method to read through rows in the database
  void readRecord(ActionEvent event) {
    final String selectQuery =
        "SELECT authorID, firstName, lastName FROM authors";
    dbnum++;

    String username = "deitel";
    String password = "deitel";

    try (
        //Making the connection to the database using the dburl, username, and password
        Connection connection = DriverManager.getConnection(dburl, username, password);
        Statement statement = connection
            .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet resultSet = statement.executeQuery(selectQuery)) {
      if (resultSet.next()) {
        resultSet.absolute(dbnum);
      }

      //retrieving the first and last name from the database
      if (resultSet.next()) {
        lblFirst.setText(resultSet.getString(2));
        lblLast.setText(resultSet.getString(3));
      } else {
        resultSet.first();
        dbnum = 1;
        lblFirst.setText(resultSet.getString(2));
        lblLast.setText(resultSet.getString(3));
      }
    }//Auto closing the resultSet using AutoClosable's methods
    catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }

  @FXML
    //Method to update existing records in the database
  void updateRecord(ActionEvent event) {
    String updateQuery = String.format("UPDATE AUTHORS SET FIRSTNAME = '%s', LASTNAME = '%s' "
            + "WHERE FIRSTNAME = '%s' AND LASTNAME = '%s'", txtFirst.getText(), txtLast.getText(),
        lblFirst.getText(), lblLast.getText());
    connect(updateQuery);
  }


  @FXML
  void connect(String query) {
    try (
        //Connecting to the derby database
        Connection connection = DriverManager.getConnection(
            //Entering the predefined username and password
            dburl, "deitel", "deitel");
        Statement statement = connection.createStatement();) {
      try {
        //Executing the query variable
        statement.execute(query);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }
}
