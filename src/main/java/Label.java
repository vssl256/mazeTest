import java.util.List;

import static org.lwjgl.opengl.GL20C.glUseProgram;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

public class Label {

    private final Font font;
    public String string;
    public int x, y;
    public float size;
    public List<Quad> quads;

    public Label( Font font, String string, int x, int y, float size ) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.size = size;

        quads = font.createLabel( x, y, string, size );
    }

    public void set( String newString ) {
        string = newString;
        quads = font.createLabel( x, y, string, size );
    }

    public void draw() {
        for ( Quad quad : quads ) {
            quad.draw();
        }
    }
}
