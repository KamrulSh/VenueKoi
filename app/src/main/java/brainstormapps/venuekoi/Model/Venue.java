package brainstormapps.venuekoi.Model;

public class Venue {
    private String Name, Price;
    private String Image, Detail, Location;

    public Venue() {
    }

    public Venue(String name, String price, String image, String detail, String location) {
        Name = name;
        Price = price;
        Image = image;
        Detail = detail;
        Location = location;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }
}
