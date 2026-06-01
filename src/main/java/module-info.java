module com.example.fnpj2048javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fnpj2048javafx to javafx.fxml;
    exports com.example.fnpj2048javafx;
}