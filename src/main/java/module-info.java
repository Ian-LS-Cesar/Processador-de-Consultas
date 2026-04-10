module com.grupo8.processadorconsultas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.grupo8.processadorconsultas to javafx.fxml;
    exports com.grupo8.processadorconsultas;
}