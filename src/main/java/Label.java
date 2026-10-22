import java.util.ArrayList;
import java.util.List;

public class Label {

    private final Font font;
    public String string;
    public int x, y;
    public float size;
    public List<Quad> quads;

    public Label( Font font, String string, int x, int y, float size ) {
        this.font = font;
        this.string = string;
        this.x = x;
        this.y = y;
        this.size = size;

        quads = font.createQuads( x, y, string, size );
    }


    public void set( String newString ) {
        string = newString;
        quads = font.createQuads( x, y, string, size );
    }
}
