import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    private final float fov;
    private final float zNear;
    private final float zFar;

    private Matrix4f modelMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    private Matrix4f modelViewMatrix;

    private Matrix4f orthoMatrix;

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Transformation( float fov, float aspect, float zNear, float zFar ) {
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;

        modelMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f().setPerspective(
                Math.toRadians( fov ), aspect, zNear, zFar
        );
        modelViewMatrix = new Matrix4f();
    }

    public void updateModel( Vector3f translation, float scale, Vector3f rotation ) {
        modelMatrix.identity()
                .translate( translation )
                .scale( scale )
                .rotateXYZ( rotation );
    }

    public void updateAspect( float aspect ) {
        projectionMatrix.setPerspective(
                Math.toRadians( fov ),
                aspect,
                zNear,
                zFar
        );
    }
}
