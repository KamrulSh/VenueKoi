package brainstormapps.venuekoi.Model;

public class VenueRequest {
    private String BookingId, Uname, Uphone, Vname, Vprice, Bdate;

    public VenueRequest() {
    }

    public VenueRequest(String bookingId, String uname, String uphone, String vname, String vprice, String bdate) {
        BookingId = bookingId;
        Uname = uname;
        Uphone = uphone;
        Vname = vname;
        Vprice = vprice;
        Bdate = bdate;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getUphone() {
        return Uphone;
    }

    public void setUphone(String uphone) {
        Uphone = uphone;
    }

    public String getVname() {
        return Vname;
    }

    public void setVname(String vname) {
        Vname = vname;
    }

    public String getVprice() {
        return Vprice;
    }

    public void setVprice(String vprice) {
        Vprice = vprice;
    }

    public String getBdate() {
        return Bdate;
    }

    public void setBdate(String bdate) {
        Bdate = bdate;
    }
}
