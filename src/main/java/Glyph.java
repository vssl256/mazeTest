public class Glyph {
    public int id;
    public int x, y;
    public int atlasX, atlasY;
    public int width, height;

    public Glyph( int id, int x, int y, int atlasX, int atlasY, int width, int height ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.atlasX = atlasX;
        this.atlasY = atlasY;
        this.width = width;
        this.height = height;
    }
}
