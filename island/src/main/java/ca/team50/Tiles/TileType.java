package ca.team50.Tiles;

public class TileType {

    private final int[] tileColour;
    private final Tile tileType;

    public int[] getTileColour() {
        return this.tileColour;
    }
    public Tile getTileType() {
        return this.tileType;
    };

    public TileType(Tile tileType, int[] tileColour) {
        this.tileColour = tileColour;
        this.tileType = tileType;
    }

}
