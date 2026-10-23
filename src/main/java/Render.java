import java.util.List;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL20C.glUseProgram;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

public class Render {

    private final GlfwWindow win;
    private final List<Quad> quads;
    private final List<Label> labels;

    public Render( GlfwWindow win, List<Quad> quads, List<Label> labels ) {
        this.win = win;
        this.quads = quads;
        this.labels = labels;
    }

    public void render() {
        win.clear();

        quads.forEach( Quad::draw );

        labels.forEach( Label::draw );

        win.swapBuffers();
    }
}
