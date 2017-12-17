package gui;

import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import program.Edge;
import program.Main;
import program.Packet;
import program.Request;
import program.Vertex;

/**
 * Hlavní zobrazovací tøída aplikace
 * 
 * @author Jakub Vítek
 * @author Jitka Poubová
 *
 */
public final class MainView {

	/** Jedináèek */
	private static MainView instance;

	/** Vyvtvoøená scéna */
	private Scene scene;

	/** Souèasný stav - MainList */
	private String currentMain;
	@SuppressWarnings("rawtypes")
	private ListView currentMainList;

	/** Souèasný stav - content list */
	private String currentContent;

	/** Souèasná data v 2. sloupci? */
	@SuppressWarnings("rawtypes")
	private ListView currentContentList;

	/** Pøístup k simulation step labelu */
	private Label labelStepNumber;

	/**
	 * Konstruktor
	 */
	private MainView() {

	}

	/**
	 * Pøekreslení aplikace
	 * 
	 * @return pøekreslená scéna
	 */
	public Scene Refresh() {

		// Main.graph.getEdges().get(0).flowInc(50);

		// System.out.println("Refresh");

		this.scene = this.setup();

		// Výbìr menu pøed refreshem
		for (int i = 0; i < currentMainList.getItems().size(); i++) {
			// System.out.println(currentMainList.getItems().get(i));

			if (currentMainList.getItems().get(i).toString().equalsIgnoreCase(currentMain)) {
				currentMainList.getSelectionModel().select(i);
			}
		}

		// Výbìr vedlejšího menu pøed Refreshem
		for (int i = 0; i < this.currentContentList.getItems().size(); i++) {

			if (currentContentList.getItems().get(i).toString().equalsIgnoreCase(currentContent)) {
				currentContentList.getSelectionModel().select(i);
			}
		}

		return this.scene;

	}

	/**
	 * Vytvoøení Scene z BorderPane
	 * 
	 * @return vytvoøená scéna
	 */
	public Scene setup() {
		BorderPane mainPane = new BorderPane();

		mainPane.setTop(this.setupTop());
		mainPane.setLeft(this.setupLeft());
		mainPane.setCenter(this.setupCenter());
		mainPane.setRight(this.setupRight());
		mainPane.setBottom(this.setupBottom());

		this.scene = new Scene(mainPane, 1000, 600);

		return this.scene;

	}

	/**
	 * Getter pro instanci
	 * 
	 * @return instance typu MainView
	 */
	public static MainView getInstance() {
		if (MainView.instance == null) {
			MainView.instance = new MainView();

		}
		return MainView.instance;
	}

	/**
	 * Nastavení horní èásti BorderLayoutu
	 * 
	 * @return vytvoøená struktura
	 */
	private Node setupTop() {

		// Vytvoøení struktury do které lze ukládat menu
		MenuBar menu = new MenuBar();

		// Vytvoøení jednotlivých menu
		Menu file = new Menu("File");

		Menu help = new Menu("Help");

		// Pøidání položky pro ukonèení aplikace
		MenuItem fileExit = new MenuItem("Exit");
		fileExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Platform.exit();
				System.exit(0);
			}

		});

		Image shutdownIcon = new Image(getClass().getResource("/gui/img/shutdown.png").toExternalForm());
		ImageView shutdownView = new ImageView(shutdownIcon);
		shutdownView.setFitWidth(15);
		shutdownView.setFitHeight(15);
		fileExit.setGraphic(shutdownView);

		// Pøidání položky pro zobrazení autorù
		MenuItem helpAbout = new MenuItem("About");
		helpAbout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Alert a = new Alert(AlertType.INFORMATION);
				a.titleProperty().set("Help > About");
				a.setHeaderText("Authors: ");
				a.setContentText(
						"Application:\nJakub Vítek - A16B0165P \nJitka Poubová - A16B0110P\n\nIcon authors:\nFreepik - www.flaticon.com\nHanan - www.flaticon.com\nSmashicons - www.flaticons.com\n\nApplication is licensed under MIT license.\nIcons are licensed under Creative Commons BY 3.0 license");

				// Icon
				Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
				Image icon16 = new Image(getClass().getResource("/gui/img/info.png").toExternalForm());
				stage.getIcons().add(icon16);

				a.showAndWait();
			}

		});

		Image aboutIcon = new Image(getClass().getResource("/gui/img/info.png").toExternalForm());
		ImageView aboutView = new ImageView(aboutIcon);
		aboutView.setFitWidth(15);
		aboutView.setFitHeight(15);
		helpAbout.setGraphic(aboutView);

		// Refresh
		MenuItem fileRefresh = new MenuItem("Refresh");
		Image imageRefresh = new Image(getClass().getResource("/gui/img/refresh.png").toExternalForm());
		ImageView refreshView = new ImageView(imageRefresh);
		refreshView.setFitWidth(15);
		refreshView.setFitHeight(15);
		fileRefresh.setGraphic(refreshView);

		fileRefresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				GUI.Refresh();
			}

		});

		// Simulate menu button
		MenuItem fileSimulate = new MenuItem("Simulate");
		fileSimulate.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

		fileSimulate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						Main.simulation.prepare();

						while (!Main.simulation.isDone()) {

							Main.simulation.simulate(Main.simulationStep);
							Main.simulationStep++;

							if (Main.simulationStep == 2) {
								break;
							}

						}

					}
				});
			}

		});

		// Pøidání položek do menu help
		help.getItems().add(helpAbout);

		// Pøidání položek menu do file
		file.getItems().add(fileSimulate);
		file.getItems().add(fileRefresh);
		file.getItems().add(new SeparatorMenuItem());
		file.getItems().add(fileExit);

		// Pøidání menu do menu baru
		menu.getMenus().add(file);
		menu.getMenus().add(help);

		return menu;

	}

	/**
	 * Nastavení levé èásti BorderLayoutu
	 * 
	 * @return vytvoøená struktura
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Node setupLeft() {

		HBox mainBox = new HBox();

		// List hlavního menu
		ListView mainList = new ListView();
		this.currentMainList = mainList;
		mainList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		ObservableList<String> items = FXCollections.observableArrayList("Routers", "Edges", "Requests", "SmartStack");
		mainList.setItems(items);

		mainList.setCellFactory(param -> new ListCell<String>() {
			private ImageView imageView = new ImageView();

			@Override
			public void updateItem(String name, boolean empty) {
				super.updateItem(name, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {

					Image imRouter = new Image(getClass().getResource("/gui/img/router.png").toExternalForm());
					Image imEdges = new Image(getClass().getResource("/gui/img/edges.png").toExternalForm());
					Image imPackets = new Image(getClass().getResource("/gui/img/packets.png").toExternalForm());
					Image imStack = new Image(getClass().getResource("/gui/img/rectangles.png").toExternalForm());

					if ("Routers".equals(name)) {
						imageView.setImage(imRouter);
					} else if ("Edges".equals(name)) {
						imageView.setImage(imEdges);
					} else if ("Requests".equals(name)) {
						imageView.setImage(imPackets);
					} else if ("SmartStack".equalsIgnoreCase(name)) {
						imageView.setImage(imStack);
					}
					setText(name);
					setGraphic(imageView);
				}
			}
		});

		// List vedlejšího menu
		ListView contentList = new ListView<String>();
		this.currentContentList = contentList;
		contentList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		for (Vertex vx : Main.graph.getVertices()) {
			contentList.getItems().add(String.format("Router ID=%d", vx.getId()));
		}

		contentList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (contentList.getItems().size() < 1) {
					return;
				}

				if (contentList.getSelectionModel().getSelectedItem() == null) {
					return;
				}

			}
		});

		contentList.setCellFactory(param -> new ListCell<String>() {

			private ImageView imageView = new ImageView();

			@Override
			public void updateItem(String name, boolean empty) {

				super.updateItem(name, empty);

				if (empty) {
					setText(null);
					setGraphic(null);
				} else {

					HBox box = new HBox();
					box.setAlignment(Pos.CENTER_LEFT);

					if (name.startsWith("Router") || name.startsWith("SmartRouter")) {

						Image imRouter = new Image(getClass().getResource("/gui/img/router.png").toExternalForm());
						imageView.setImage(imRouter);
					} else if (name.startsWith("Edge")) {
						Image imEdge = new Image(getClass().getResource("/gui/img/edges.png").toExternalForm());
						imageView.setImage(imEdge);

					} else if (name.startsWith("Request")) {
						Image imPacket = new Image(getClass().getResource("/gui/img/packets.png").toExternalForm());
						imageView.setImage(imPacket);
					}

					Label lab = new Label(name);

					Button buttDelete = new Button();

					Image imDelete = new Image(getClass().getResource("/gui/img/delete.png").toExternalForm());
					ImageView delete = new ImageView(imDelete);
					buttDelete.setGraphic(delete);

					// Image imDeleteRed = new
					// Image(getClass().getResource("/gui/img/deleteRed.png").toExternalForm());
					// ImageView deleteRed = new ImageView(imDelete);
					buttDelete.setGraphic(delete);

					buttDelete.setStyle("-fx-background-color: transparent;");

					buttDelete.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent event) {
							Label lb = (Label) buttDelete.getParent().getChildrenUnmodifiable().get(1);

							// Smazat hranu
							if (lb.getText().startsWith("Edge")) {

								String[] mainPrt = lb.getText().split(" ");
								String[] ids = mainPrt[1].split("->");

								int start = Integer.parseInt(ids[0]);
								int end = Integer.parseInt(ids[1]);

								Alert cf = new Alert(AlertType.CONFIRMATION);
								cf.setHeaderText("Confirmation!");
								cf.setContentText(String.format(
										"Do you really want to delete this Edge (%d -> %d) from graph?", start, end));

								Optional<ButtonType> result = cf.showAndWait();

								if (result.isPresent() && result.get().getText().equalsIgnoreCase("ok")) {

									Main.graph.removeEdge(start, end);
									contentList.getItems().clear();

									// Refresh
									for (Edge ed : Main.graph.getEdges()) {
										contentList.getItems().add(String.format("Edge %d->%d ",
												ed.getStartVertex().getId(), ed.getEndVertex().getId()));
									}

								}
							}

							// Smazat Vrchol
							if (lb.getText().startsWith("Router") || lb.getText().startsWith("SmartRouter")) {
								String[] mainPrt = lb.getText().split(" ");
								String[] ids = mainPrt[1].split("=");

								int routerID = Integer.parseInt(ids[1]);

								Alert cf = new Alert(AlertType.CONFIRMATION);
								cf.setHeaderText("Confirmation!");
								cf.setContentText(String.format(
										"Do you really want to delete this Router (ID = %d) from graph?", routerID));

								Optional<ButtonType> result = cf.showAndWait();

								if (result.isPresent() && result.get().getText().equalsIgnoreCase("ok")) {

									Main.graph.removeVertex(routerID);

									contentList.getItems().clear();

									while (mainBox.getChildren().size() > 2) {
										mainBox.getChildren().remove(mainBox.getChildren().size() - 1);
									}

									// Refresh
									for (Vertex vx : Main.graph.getVertices()) {
										contentList.getItems().add(String.format("Router ID=%d", vx.getId()));
									}
								}

							}

						}
					});

					box.getChildren().add(imageView);
					box.getChildren().add(lab);

					if (!name.startsWith("Request")) {
						box.getChildren().add(buttDelete);
					}

					box.setSpacing(10);

					setGraphic(box);

				}
			}

		});

		// Výbìr z hlavního menu event
		mainList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

				if (mainList.getSelectionModel().getSelectedItem() == null) {
					return;
				}

				String vyber = mainList.getSelectionModel().getSelectedItem().toString();

				if ("Routers".equalsIgnoreCase(vyber)) {
					currentMain = "Routers";
					contentList.getItems().clear();

					for (Vertex vx : Main.graph.getVertices()) {
						contentList.getItems().add(String.format("Router ID=%d", vx.getId()));
					}

				} else if ("Edges".equalsIgnoreCase(vyber)) {
					currentMain = "Edges";

					contentList.getItems().clear();

					for (Edge ed : Main.graph.getEdges()) {
						contentList.getItems().add(
								String.format("Edge %d->%d ", ed.getStartVertex().getId(), ed.getEndVertex().getId()));
					}
				} else if ("Requests".equalsIgnoreCase(vyber)) {
					currentMain = "Requests";
					contentList.getItems().clear();

					for (Request r : Main.requests) {
						contentList.getItems().add(r.toStringShort());
					}
				} else if ("SmartStack".equalsIgnoreCase(vyber)) {
					currentMain = "SmartStack";
					contentList.getItems().clear();

					for (Vertex vx : Main.graph.getVertices()) {
						contentList.getItems().add(String.format("SmartRouter ID=%d", vx.getId()));
					}
				}

			}

		});

		mainList.setEditable(false);
		contentList.setEditable(false);

		// Pøidat do hBox
		mainBox.getChildren().add(mainList);
		mainBox.getChildren().add(contentList);

		// Fix
		mainList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// Vyhovìní PMD zpùsobuje NullPointerException - podmínka musí
				// být postavena takto - prostì musí
				if (oldValue != newValue) {
					while (mainBox.getChildren().size() > 2) {
						mainBox.getChildren().remove(mainBox.getChildren().size() - 1);
					}
				}

				if (newValue.startsWith("Edge")) {
					contentList.setVisible(false);
					contentList.setManaged(false);

					TableView<EdgeGUI> table = new TableView<EdgeGUI>();

					TableColumn<EdgeGUI, String> source = new TableColumn<EdgeGUI, String>("Source");
					source.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("source"));
					source.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> target = new TableColumn<EdgeGUI, String>("Target");
					target.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("target"));
					target.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> maxSpeed = new TableColumn<EdgeGUI, String>("Maximum speed");
					maxSpeed.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("maximumSpeed"));
					maxSpeed.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> reliability = new TableColumn<EdgeGUI, String>("Reliability");
					reliability.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("reliability"));
					reliability.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> currentFlow = new TableColumn<EdgeGUI, String>("Current flow");
					currentFlow.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("currentFlow"));
					currentFlow.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> load = new TableColumn<EdgeGUI, String>("Load");
					load.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("load"));
					load.setStyle("-fx-alignment: CENTER;");

					// Button
					TableColumn<EdgeGUI, EdgeGUI> action = new TableColumn<>("Action");

					action.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
					action.setStyle("-fx-alignment: CENTER;");
					action.setCellFactory(param -> new TableCell<EdgeGUI, EdgeGUI>() {
						private final Button deleteButton = new Button("delete selected");

						@Override
						protected void updateItem(EdgeGUI person, boolean empty) {
							super.updateItem(person, empty);

							if (person == null) {
								setGraphic(null);
								return;
							}

							deleteButton.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent e) {
									int index = getTableView().getSelectionModel().getSelectedIndex();

									if (index == -1) {
										return;
									}

									Main.graph.getEdges().remove(index);

									getTableView().getItems().clear();

									for (Edge edg : Main.graph.getEdges()) {
										EdgeGUI eg = new EdgeGUI(edg);
										table.getItems().add(eg);

									}

								}

							});

							setGraphic(deleteButton);

						}
					});

					table.getColumns().addAll(source, target, maxSpeed, reliability, currentFlow, load, action);
					table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

					for (Edge e : Main.graph.getEdges()) {
						EdgeGUI eg = new EdgeGUI(e);
						table.getItems().add(eg);

					}

					mainBox.getChildren().add(table);

				} else {

					contentList.setVisible(true);
					contentList.setManaged(true);

				}

			}
		});

		// Detail
		contentList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue == null) {
					return;
				}

				currentContent = newValue;

				// Detail hranz
				if (newValue.startsWith("Edge")) {

					while (mainBox.getChildren().size() > 2) {
						mainBox.getChildren().remove(mainBox.getChildren().size() - 1);
					}

					// Get data
					int index = contentList.getSelectionModel().getSelectedIndex();
					Edge e = Main.graph.getEdges().get(index);

					VBox vbox = new VBox();

					HBox source = new HBox();
					Label src = new Label("Source: ");
					Label srcVal = new Label(" " + e.getStartVertex().getId());
					source.getChildren().addAll(src, srcVal);
					source.setSpacing(2);

					HBox target = new HBox();
					Label trg = new Label("Target: ");
					Label trgVal = new Label(" " + e.getEndVertex().getId());
					target.getChildren().addAll(trg, trgVal);
					target.setSpacing(2);

					HBox maxSpeed = new HBox();
					Label ms = new Label("Maximum speed: ");
					Label msVal = new Label(" " + e.getMaximumSpeed());
					maxSpeed.getChildren().addAll(ms, msVal);
					maxSpeed.setSpacing(2);

					HBox reliability = new HBox();
					Label rel = new Label("Reliability: ");
					Label relVal = new Label(" " + (e.getStability() * 100) + "%");
					reliability.getChildren().addAll(rel, relVal);
					reliability.setSpacing(2);

					HBox flow = new HBox();
					Label flw = new Label("Current flow: ");
					Label flwVal = new Label(" " + e.getFlow());
					flow.getChildren().addAll(flw, flwVal);
					flow.setSpacing(2);

					// table.getColumns().addAll(source, target, maxSpeed, rel,
					// flow);

					vbox.getChildren().addAll(source, target, maxSpeed, reliability, flow);
					vbox.setSpacing(5);
					mainBox.getChildren().add(vbox);
					HBox.setMargin(vbox, new Insets(10, 0, 0, 10));

				}

				if (newValue.startsWith("Router")) {

					while (mainBox.getChildren().size() > 2) {
						mainBox.getChildren().remove(mainBox.getChildren().size() - 1);
					}

					String[] mainPrt = newValue.split(" ");
					String[] ids = mainPrt[1].split("=");

					int routerID = Integer.parseInt(ids[1]);

					TableView<EdgeGUI> table = new TableView<EdgeGUI>();

					TableColumn<EdgeGUI, String> source = new TableColumn<EdgeGUI, String>("Source");
					source.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("source"));
					source.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> target = new TableColumn<EdgeGUI, String>("Target");
					target.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("target"));
					target.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> maxSpeed = new TableColumn<EdgeGUI, String>("Maximum speed");
					maxSpeed.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("maximumSpeed"));
					maxSpeed.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> reliability = new TableColumn<EdgeGUI, String>("Reliability");
					reliability.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("reliability"));
					reliability.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> currentFlow = new TableColumn<EdgeGUI, String>("Current flow");
					currentFlow.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("currentFlow"));
					currentFlow.setStyle("-fx-alignment: CENTER;");

					TableColumn<EdgeGUI, String> load = new TableColumn<EdgeGUI, String>("Load");
					load.setCellValueFactory(new PropertyValueFactory<EdgeGUI, String>("load"));
					load.setStyle("-fx-alignment: CENTER;");

					table.getColumns().addAll(source, target, maxSpeed, reliability, currentFlow, load);
					table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

					for (Edge e : Main.graph.getEdges()) {

						if (e.getStartVertex().getId() != routerID) {
							continue;
						}

						EdgeGUI eg = new EdgeGUI(e);
						table.getItems().add(eg);

					}

					mainBox.getChildren().add(table);
				}

				// TODO: ZDE LOGIKA PRO 3. sloupec SmartRouteru
				// TODO:
				// TODO:

				if (newValue.startsWith("SmartRouter")) {

					while (mainBox.getChildren().size() > 2) {
						mainBox.getChildren().remove(mainBox.getChildren().size() - 1);
					}

					ListView stackView = new ListView();

					int selection = contentList.getSelectionModel().getSelectedIndex();
					Vertex vx = Main.graph.getVertices().get(selection);

					for (Packet p : vx.getSmartstack().getStack()) {
						stackView.getItems().add(p);
					}

					mainBox.getChildren().add(stackView);

				}

				// TODO:
				// TODO:
				// TODO:

			}

		});

		return mainBox;
	}

	/**
	 * Nastavení prostøedí èásti BorderLayoutu
	 * 
	 * @return vytvoøená struktura
	 */
	private Node setupCenter() {
		return null;
	}

	/**
	 * Nastavení pravé èásti BorderLayoutu
	 * 
	 * @return vytvoøená struktura
	 */
	private Node setupRight() {
		return null;
	}

	/**
	 * Nastavení spodní èásti BorderLayoutu
	 * 
	 * @return vytvoøená struktura
	 */
	private Node setupBottom() {

		HBox bottomControl = new HBox();
		bottomControl.setPadding(new Insets(5, 10, 5, 10));

		Label labelStep = new Label("Step: ");
		labelStep.setFont(new Font(labelStep.getFont().getName(), 28));
		labelStep.setAlignment(Pos.BASELINE_LEFT);

		labelStepNumber = new Label();
		labelStepNumber.setText("" + Main.simulationStep);
		labelStepNumber.setFont(new Font(labelStep.getFont().getName(), 28));
		labelStepNumber.setAlignment(Pos.BASELINE_LEFT);

		Separator sep1 = new Separator();

		Button buttonSimulate = new Button("Simulate");
		buttonSimulate.setFont(new Font(labelStep.getFont().getName(), 22));
		Image simImg = new Image(getClass().getResource("/gui/img/next-track-button.png").toExternalForm());
		ImageView simView = new ImageView(simImg);
		buttonSimulate.setGraphic(simView);

		// Akce simulace
		buttonSimulate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				Alert a = new Alert(AlertType.CONFIRMATION);
				a.setHeaderText("Confirmation: ");
				a.setContentText(
						"Do you really like to simulate whole process in one go? (It may take a long time and render this GUI application inoperable!");

				Optional<ButtonType> result = a.showAndWait();

				if (result.isPresent() && result.get().getText().equalsIgnoreCase("ok")) {

					Main.simulation.start();
					Main.fileHandler.writeSimulation(
							"" + Main.simulation.printSimulationInfo() + Main.simulation.printRequests(),
							Main.fn_out_sim);
				}

			}

		});

		Button buttonStep = new Button("Step");
		buttonStep.setFont(new Font(labelStep.getFont().getName(), 22));
		Image stepImg = new Image(getClass().getResource("/gui/img/right-arrow.png").toExternalForm());
		ImageView stepView = new ImageView(stepImg);
		buttonStep.setGraphic(stepView);

		buttonStep.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (Main.simulationStep == 0) {
					Main.simulation.prepare();
				}

				Main.simulation.simulate(Main.simulationStep);

				// Update GUI
				GUI.Refresh();

				Main.simulationStep++;

			}

		});
		// Pøidat prvky
		bottomControl.getChildren().add(labelStep);
		bottomControl.getChildren().add(labelStepNumber);
		bottomControl.getChildren().add(sep1);
		bottomControl.getChildren().add(buttonStep);
		bottomControl.getChildren().add(buttonSimulate);

		return bottomControl;
	}

}
