import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameLibraryApp extends Application {
    private List<Game> gameLibrary = new ArrayList<>();
    private List<Game> personalLibrary = new ArrayList<>();

    private Scene gameLibraryScene;
    private Scene personalLibraryScene;

    private ListView<HBox> gameListView = new ListView<>();
    private ListView<HBox> personalListView = new ListView<>();

    @Override
    public void start(Stage primaryStage) {
        // Initialize game library
        initializeGameLibrary();

        // Set up Game Library Scene
        VBox gameLibraryLayout = createGameLibraryLayout(primaryStage);

        // Set up Personal Library Scene
        VBox personalLibraryLayout = createPersonalLibraryLayout(primaryStage);

        gameLibraryScene = new Scene(gameLibraryLayout, 800, 600);
        personalLibraryScene = new Scene(personalLibraryLayout, 800, 600);

        primaryStage.setTitle("Game Library App");
        primaryStage.setScene(gameLibraryScene);
        primaryStage.show();
    }

    private VBox createGameLibraryLayout(Stage primaryStage) {
        Label title = new Label("Game Library");
        Button toLibraryButton = new Button("Your Library");
        toLibraryButton.setOnAction(e -> primaryStage.setScene(personalLibraryScene));

        // ComboBox for genre filtering
    ComboBox<String> genreDropdown = new ComboBox<>();
    genreDropdown.getItems().add("All"); // Default option to show all games
    genreDropdown.getItems().addAll(getAllGenres()); // Add all unique genres
    genreDropdown.setValue("All"); // Set default selection to "All"

    // Event handler for genre selection
    genreDropdown.setOnAction(e -> filterGamesByGenre(genreDropdown.getValue()));

    // Layout for the top controls
    HBox topControls = new HBox(10, toLibraryButton, genreDropdown);
    topControls.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");



        // Populate the ListView with games and their images
        gameListView.getItems().clear();
        for (Game game : gameLibrary) {
            HBox gameRow = createGameRow(game, false);
            gameListView.getItems().add(gameRow);
        }

        ScrollPane scrollPane = new ScrollPane(gameListView);
        scrollPane.setFitToWidth(true);

        VBox layout = new VBox(10, topControls, title, scrollPane);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: top-center;");
        return layout;
    }


    private ComboBox<String> personalLibraryGenreDropdown = new ComboBox<>();

    private VBox createPersonalLibraryLayout(Stage primaryStage) {
        Label title = new Label("Your Library");
        Button toGameLibraryButton = new Button("Game Library");
        toGameLibraryButton.setOnAction(e -> primaryStage.setScene(gameLibraryScene));
        

        // Initialize ComboBox for genre filtering in Your Library
    personalLibraryGenreDropdown.getItems().add("All"); // Default option to show all games
    personalLibraryGenreDropdown.setValue("All"); // Set default selection to "All"

    // Event handler for genre selection
    personalLibraryGenreDropdown.setOnAction(e -> filterPersonalLibraryByGenre(personalLibraryGenreDropdown.getValue()));

    // Layout for the top controls
    HBox topControls = new HBox(10, toGameLibraryButton, personalLibraryGenreDropdown);
    topControls.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");



        refreshPersonalLibrary();

        // Populate personal library ListView
        personalListView.getItems().clear();
        for (Game game : personalLibrary) {
            HBox gameRow = createGameRow(game, true);
            personalListView.getItems().add(gameRow);
        }

        ScrollPane scrollPane = new ScrollPane(personalListView);
        scrollPane.setFitToWidth(true);

        VBox layout = new VBox(10, topControls, title, scrollPane);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: top-center;");
        return layout;
    }

    private void refreshPersonalLibrary() {
        personalListView.getItems().clear(); // Clear the current items
        for (Game game : personalLibrary) {
            HBox gameRow = createGameRow(game, true); // Create rows with "Remove" buttons
            personalListView.getItems().add(gameRow);
        }
        updatePersonalLibraryGenres();
    }

    private void filterGamesByGenre(String genre) {
        gameListView.getItems().clear();
    
        for (Game game : gameLibrary) {
            if (genre.equals("All") || game.getGenre().equalsIgnoreCase(genre)) {
                HBox gameRow = createGameRow(game, false);
                gameListView.getItems().add(gameRow);
            }
        }
    }

    private List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        for (Game game : gameLibrary) {
            if (!genres.contains(game.getGenre())) {
                genres.add(game.getGenre());
            }
        }
        return genres;
    }



    private List<String> getGenresFromPersonalLibrary() {
        List<String> genres = new ArrayList<>();
        for (Game game : personalLibrary) {
            if (!genres.contains(game.getGenre())) {
                genres.add(game.getGenre());
            }
        }
        return genres;
    }

    private void updatePersonalLibraryGenres() {
        personalLibraryGenreDropdown.getItems().clear(); // Clear existing items
        personalLibraryGenreDropdown.getItems().add("All"); // Add "All" option
    
        // Add unique genres from the personal library
        for (String genre : getGenresFromPersonalLibrary()) {
            if (!personalLibraryGenreDropdown.getItems().contains(genre)) {
                personalLibraryGenreDropdown.getItems().add(genre);
            }
        }
    }


    private void filterPersonalLibraryByGenre(String genre) {
        personalListView.getItems().clear();
    
        for (Game game : personalLibrary) {
            if (genre.equals("All") || game.getGenre().equalsIgnoreCase(genre)) {
                HBox gameRow = createGameRow(game, true);
                personalListView.getItems().add(gameRow);
            }
        }
    }







    private HBox createGameRow(Game game, boolean isPersonalLibrary) {
        ImageView gameImageView = new ImageView(new Image(game.getImagePath()));
        gameImageView.setFitWidth(100);
        gameImageView.setFitHeight(100);
        gameImageView.setPreserveRatio(true);

        Label gameDetails = new Label(game.getTitle() + "\n" 
                                + game.getGenre() + "\n" 
                                + game.getDescripton());
        gameDetails.setStyle("-fx-padding: 10px;");

        Button actionButton = new Button(isPersonalLibrary ? "Remove" : "Add");
    if (isPersonalLibrary) {
        actionButton.setOnAction(e -> {
            personalLibrary.remove(game); // Remove from personal library
            refreshPersonalLibrary();    // Refresh the view
        });
    } else {
        actionButton.setOnAction(e -> {
            if (!personalLibrary.contains(game)) {
                personalLibrary.add(game); // Add to personal library
                refreshPersonalLibrary();  // Refresh the view
            }
        });
    }

        HBox gameRow = new HBox(10, gameImageView, gameDetails, actionButton);
        gameRow.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");

        return gameRow;
    }

    private void initializeGameLibrary() {
        gameLibrary.add(new Game("The Legend of Zelda", "Adventure", "images/zelda.png", "Explore the kingdom of Hyrule and save Princess Zelda."));
        gameLibrary.add(new Game("Call of Duty", "Shooter", "images/callofduty.png", "Engage in intense military battles and missions."));
        gameLibrary.add(new Game("Super Mario Bros", "Platformer", "images/mario.png", "Join Mario and Luigi in their quest to rescue Princess Peach."));
        gameLibrary.add(new Game("Minecraft", "Sandbox", "images/minecraft.png", "Build, explore, and survive in a blocky 3D world."));
        gameLibrary.add(new Game("Dark Souls", "RPG", "images/darksouls.png", "Venture through a dark fantasy world filled with challenges."));
        gameLibrary.add(new Game("Overwatch", "Shooter", "images/overwatch.png", "Join the fight in a fast-paced, team-based FPS."));
        gameLibrary.add(new Game("FIFA 23", "Sports", "images/fifa.png", "Experience realistic soccer matches with your favorite teams."));
        gameLibrary.add(new Game("The Sims 4", "Simulation", "images/sims.png", "Create and control your own virtual people in life-like scenarios."));
        gameLibrary.add(new Game("League of Legends", "MOBA", "images/league.png", "Compete in strategic battles with unique champions."));
        gameLibrary.add(new Game("Fortnite", "Battle Royale", "images/fortnite.png", "Drop into a 100-player battle royale and fight to be the last one standing."));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
