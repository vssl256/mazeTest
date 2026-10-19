import java.util.ArrayList;
import java.util.List;

import static utils.FileExtensions.*;

public class Render {

    private final GlfwWindow win;
    private final List<Quad> quads = new ArrayList<>();

    public Render( GlfwWindow win ) {
        this.win = win;
        Shader shader = new Shader( "default" );
        Texture texture2 = new Texture( "null", png );
        Texture texture = new Texture( "test", png );
        Texture texture1 = new Texture( "test2", png );
        Quad quad = new Quad( new float[] {
                1.0f,   1.0f,   0.0f,       1.0f,   1.0f,
                1.0f,   -1.0f,  0.0f,       1.0f,   0.0f,
                -1.0f,  -1.0f,  0.0f,       0.0f,   0.0f,
                -1.0f,  1.0f,   0.0f,       0.0f,   1.0f
        }, shader, texture );
        Quad quad2 = new Quad( new float[] {
                -0.8f,   1.0f,   0.0f,       1.0f,   1.0f,
                -0.8f,   0.8f,  0.0f,       1.0f,   0.0f,
                -1.0f,  0.8f,  0.0f,       0.0f,   0.0f,
                -1.0f,  1.0f,   0.0f,       0.0f,   1.0f
        }, shader, texture2 );
        Quad quad3 = new Quad( new float[] {
                0.8f,   1.0f,   0.0f,       1.0f,   1.0f,
                0.8f,   0.8f,  0.0f,       1.0f,   0.0f,
                1.0f,  0.8f,  0.0f,       0.0f,   0.0f,
                1.0f,  1.0f,   0.0f,       0.0f,   1.0f
        }, shader, texture1 );

        quads.add( quad );
        quads.add( quad2 );
        quads.add( quad3 );
    }

    public void render() {
        win.clear();

        for ( Quad quad : quads ) {
            quad.draw();
        }

        win.swapBuffers();
    }
}
