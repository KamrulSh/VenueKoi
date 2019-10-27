package brainstormapps.venuekoi.Model;

public class Venue {
    private String Name;
    private String Image;

    public Venue() {
    }

    public Venue(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
