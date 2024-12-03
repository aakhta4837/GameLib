import javafx.scene.image.Image;

public class Game {
    private String title;
    private String genre;
    private String imagePath;
    private String description;

    public Game(String title, String genre, String imagePath, String description) {
        this.title = title;
        this.genre = genre;
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getImagePath() {
        return imagePath;
    }
    public String getDescripton() {
        return description;
    }

    @Override
    public String toString() {
        return title + " (" + genre + ")";
    }
}
