package model;

public class Route {
    private int id;
    private String source;
    private String destination;

    public Route() {}

    public Route(int id, String source, String destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }

    public Route(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    @Override
    public String toString() {
        return id + " - " + source + " ➜ " + destination;
    }
}