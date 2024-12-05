module dat044.lab2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.httpserver;


    opens dat044.lab2 to javafx.fxml;
    exports dat044.lab2;
}