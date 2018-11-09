package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

  @FXML
  private Label lblFirst;

  @FXML
  private Label lblLast;

  @FXML
  void connect(ActionEvent event) {

    final String DATABASE_URL = "jdbc:derby:lib//books";
    final String SELECT_QUERY =
        "SELECT authorID, firstName, lastName FROM authors";

    // use try-with-resources to connect to and query the database
    try (
        Connection connection = DriverManager.getConnection(
            DATABASE_URL, "deitel", "deitel");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY)) {
      resultSet.next();
      lblFirst.setText(resultSet.getString(2));
      lblLast.setText(resultSet.getString(3));
    }
    // AutoCloseable objects' close methods are called now
    catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
  }
}
