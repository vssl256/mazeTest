import java.util.List;

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
