import java.util.List;

public class Label {

    private final Font font;
    public String string;
    public int x, y;
    public List<QuadDepr> quadDeprs;
    private TextMesh textMesh;

    public Label( Font font, String string, int x, int y ) {
        this.font = font;
        this.x = x;
        this.y = y;

        textMesh = new TextMesh( x, y, string, font );
    }

    public void set( String newString ) {
        string = newString;
        textMesh = new TextMesh( x, y, string, font );
    }

    public void draw() {
        textMesh.draw();
    }
}
