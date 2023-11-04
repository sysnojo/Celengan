package Controller;

import View.Login_Register.Login_form;
import View.Login_Register.Registration_form;
import View.Splash_Screen.SplashScreen;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;

    public SceneController(Stage stage) {
        this.stage = stage;
    }

    /* LOGIN_REGISTRATION */
    // Registration
    public void switchToRegistration() {
        Registration_form registration_form = new Registration_form(this.stage);
        // Stage registrationStage = new Stage(); // create a new Stage object
        // registration_form.start(registrationStage); // pass the Stage object to the
        // start method
        registration_form.start();
    }

    // Login
    public void switchToLogin() {
        Login_form login_form = new Login_form(this.stage);
        // Stage loginStage = new Stage(); // create a new Stage object
        // login_form.start(loginStage); // pass the Stage object to the start method
        login_form.start();
    }

    /* SPLASH SCREEN */
    public void switchToSplashScreen() {
        SplashScreen splashScreen = new SplashScreen(this.stage);
    }

}
