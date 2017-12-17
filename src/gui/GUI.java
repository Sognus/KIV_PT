package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Grafické uživatelské rozhraní
 * 
 * @author Jakub Vítek
 *
 */
public class GUI extends Application {

	/** Jedináèek aplikace - mùžeme používat pouze jedno okno */
	private static GUI application;

	/** Souèasnì používaná instance Stage */
	private Stage primaryStage;

	/**
	 * Zapnutí aplikace staticky
	 * 
	 */
	public static void start() {
		launch();
	}

	/**
	 * Zapnutí aplikace (okna)
	 * 
	 */
	@Override
	public void start(Stage stage) throws Exception {
		GUI.application = this;
		this.primaryStage = stage;

		Scene scene = MainView.getInstance().setup();

		this.primaryStage.setScene(scene);
		this.primaryStage.setTitle("Simulace poèítaèové sítì");
		this.primaryStage.setResizable(true);

		Image icon = new Image(getClass().getResource("/gui/img/icon.png").toExternalForm());
		stage.getIcons().add(icon);

		this.primaryStage.show();

		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		});
		
		// VYMAZAT !!! - Pouze debug
		/*new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				System.exit(0);
			}
		}, 300 * 1000); */
	}

	/**
	 * Metoda pro pøekreslení aplikace pro provedení simulaèního kroku
	 * 
	 */
	public static void Refresh() {
		GUI.getInstance().primaryStage.setScene(MainView.getInstance().Refresh());
	}

	/**
	 * Získání instance
	 * 
	 * @return instance GUI
	 */
	public static GUI getInstance() {
		return GUI.application;
	}

}
