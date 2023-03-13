package shared;

import java.io.Serializable;

public class Genre implements Serializable {

    private String genreName;

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String toString() {
        return getGenreName();
    }
}
