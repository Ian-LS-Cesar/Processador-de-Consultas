module com.grupo8.processadorconsultas {
    requires javafx.controls;
    requires javafx.fxml;

    // Essencial para o JavaFX conseguir instanciar sua classe MainApp
    opens com.grupo8.processadorconsultas.view to javafx.graphics;

    // Caso você use FXML no futuro
    opens com.grupo8.processadorconsultas to javafx.fxml;
    exports com.grupo8.processadorconsultas.view;
}