package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/View/UIAdminNegocio.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1300, 800);

        // 👉 Conectás el CSS acá
        scene.getStylesheets().add(App.class.getResource("/Estilos/estilos.css").toExternalForm());

        stage.setTitle("Que lees virgo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
