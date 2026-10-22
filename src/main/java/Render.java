import java.util.ArrayList;
import java.util.List;

import static utils.FileExtensions.*;
import static utils.FileTypes.TEXTURE;

public class Render {

    private final GlfwWindow win;
    private final List<Quad> quads = new ArrayList<>();
    private List<Label> labels;

    public Render( GlfwWindow win, List<Label> labels ) {
        this.win = win;
        this.labels = labels;
        Shader shader = new Shader( "default" );
        Texture texture = new Texture( "test", png, TEXTURE );

        Quad quad = new Quad( new float[] {
                1.0f,   1.0f,   0.0f,       1.0f,   1.0f,
                1.0f,   -1.0f,  0.0f,       1.0f,   0.0f,
                -1.0f,  -1.0f,  0.0f,       0.0f,   0.0f,
                -1.0f,  1.0f,   0.0f,       0.0f,   1.0f
        }, shader, texture );

        quads.add( quad );
        //quads.addAll( font.createQuads( 0, 0, string, 1 ) );
    }

    public void render() {
        win.clear();

        for ( Quad quad : quads ) {
            quad.draw();
        }
        if ( labels != null ) {
            for ( Label label : labels ) {
                if ( label.quads == null ) continue;
                for ( Quad quad : label.quads ) {
                    quad.draw();
                }
            }
        }

        win.swapBuffers();
    }
}
