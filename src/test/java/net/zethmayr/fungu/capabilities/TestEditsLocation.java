package net.zethmayr.fungu.capabilities;

public class TestEditsLocation implements HasLocation, SetsLocation, EditsLocation {

    private String location;


    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(final String location) {
        this.location = location;
    }
}
