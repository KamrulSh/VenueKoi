package brainstormapps.venuekoi.Model;

public class VenueModel {
    private String CatId, Name, Price;
    private String Image, Detail, Location;

    public VenueModel() {
    }

    public VenueModel(String catId, String name, String price, String image, String detail, String location) {
        CatId = catId;
        Name = name;
        Price = price;
        Image = image;
        Detail = detail;
        Location = location;
    }


    public String getCatId() {
        return CatId;
    }

    public void setCatId(String catId) {
        CatId = catId;
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
