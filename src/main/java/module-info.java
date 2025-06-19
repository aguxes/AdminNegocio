module org.example.adminnegocio10 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens View to javafx.fxml;
    exports View;
}