package brainstormapps.venuekoi.Model;

public class RatingModel {
    private String userPhone, venueId, rateValue, userComment;

    public RatingModel() {
    }

    public RatingModel(String userPhone, String venueId, String rateValue, String userComment) {
        this.userPhone = userPhone; // both key and value
        this.venueId = venueId;
        this.rateValue = rateValue;
        this.userComment = userComment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
}
