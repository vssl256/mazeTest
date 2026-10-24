import java.util.List;

import static org.lwjgl.opengl.GL40C.glUseProgram;

public class Render {

    private final GlfwWindow win;
    private final Transformation transformation;
    private final List<QuadMesh> quadMeshes;
    private final List<Label> labels;

    public Render( GlfwWindow win, Transformation transformation, List<QuadMesh> quadMeshes, List<Label> labels ) {
        this.win = win;
        this.transformation = transformation;
        this.quadMeshes = quadMeshes;
        this.labels = labels;
    }

    public void render() {
        win.clear();

        updateMVPMatrix();
        quadMeshes.forEach( QuadMesh::draw );

        labels.forEach( Label::draw );


        win.swapBuffers();
    }

    public void updateMVPMatrix() {
        Shader shader = quadMeshes.getFirst().shader;
        shader.set( "viewMatrix", transformation.getViewMatrix() );
        shader.set( "projectionMatrix", transformation.getProjectionMatrix() );
    }
}
