package consumer;

public class Movie {
    private String title;
    private int releaseYear;
    private float score;
    private int numberOfVotes;
    private int duration;
    private String mainGenre;
    private String mainProduction;

    public Movie() {}

    public Movie(String title, int releaseYear, float score, int numberOfVotes, int duration, String mainGenre, String mainProduction) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.score = score;
        this.numberOfVotes = numberOfVotes;
        this.duration = duration;
        this.mainGenre = mainGenre;
        this.mainProduction = mainProduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMainGenre() {
        return mainGenre;
    }

    public void setMainGenre(String mainGenre) {
        this.mainGenre = mainGenre;
    }

    public String getMainProduction() {
        return mainProduction;
    }

    public void setMainProduction(String mainProduction) {
        this.mainProduction = mainProduction;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", score=" + score +
                ", numberOfVotes=" + numberOfVotes +
                ", duration=" + duration +
                ", mainGenre='" + mainGenre + '\'' +
                ", mainProduction='" + mainProduction + '\'' +
                '}';
    }
}
