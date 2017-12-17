package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Grafick� u�ivatelsk� rozhran�
 * 
 * @author Jakub V�tek
 *
 */
public class GUI extends Application {

	/** Jedin��ek aplikace - m��eme pou��vat pouze jedno okno */
	private static GUI application;

	/** Sou�asn� pou��van� instance Stage */
	private Stage primaryStage;

	/**
	 * Zapnut� aplikace staticky
	 * 
	 */
	public static void start() {
		launch();
	}

	/**
	 * Zapnut� aplikace (okna)
	 * 
	 */
	@Override
	public void start(Stage stage) throws Exception {
		GUI.application = this;
		this.primaryStage = stage;

		Scene scene = MainView.getInstance().setup();

		this.primaryStage.setScene(scene);
		this.primaryStage.setTitle("Simulace po��ta�ov� s�t�");
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
	 * Metoda pro p�ekreslen� aplikace pro proveden� simula�n�ho kroku
	 * 
	 */
	public static void Refresh() {
		GUI.getInstance().primaryStage.setScene(MainView.getInstance().Refresh());
	}

	/**
	 * Z�sk�n� instance
	 * 
	 * @return instance GUI
	 */
	public static GUI getInstance() {
		return GUI.application;
	}

}
