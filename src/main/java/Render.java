import java.util.ArrayList;
import java.util.List;

import static utils.FileExtensions.*;
import static utils.FileTypes.FONT;
import static utils.FileTypes.TEXTURE;

public class Render {

    private final GlfwWindow win;
    private final List<Quad> quads = new ArrayList<>();

    public Render( GlfwWindow win ) {
        this.win = win;
        Shader shader = new Shader( "default" );
        Texture texture = new Texture( "test", png, TEXTURE );

        Font font = new Font( "test", shader );

        Quad quad = new Quad( new float[] {
                1.0f,   1.0f,   0.0f,       1.0f,   1.0f,
                1.0f,   -1.0f,  0.0f,       1.0f,   0.0f,
                -1.0f,  -1.0f,  0.0f,       0.0f,   0.0f,
                -1.0f,  1.0f,   0.0f,       0.0f,   1.0f
        }, shader, texture );

        quads.add( quad );
        quads.addAll( font.write( 0, 0, "test string " ) );
    }

    public void render() {
        win.clear();

        for ( Quad quad : quads ) {
            quad.draw();
        }

        win.swapBuffers();
    }
}
