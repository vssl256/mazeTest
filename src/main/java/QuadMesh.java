import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL30C.*;

public class QuadMesh {
    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_HEIGHT = 1080;

    private final Texture texture;
    private final Transformation transformation;
    public final Shader shader;

    List<Quad> quads;
    private int[] indices;
    private FloatBuffer vertices;

    private int VAO;
    private int VBO;
    private int EBO;

    public QuadMesh( List<Quad> quads, Texture texture, Shader shader, Transformation transformation ) {
        this.quads = quads;
        this.texture = texture;
        this.shader = shader;
        this.transformation = transformation;

        generateIndices();
        generateVertices();
        init();
    }

    Vector3f translation;
    float rotate = 0.0f;
    Vector3f rotation;
    public void draw() {
        glUseProgram( shader.id );
        glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_2D, texture.id );
        glBindVertexArray( VAO );

        translation = new Vector3f( 0.0f, 0.0f, 0.0f );
        rotation = new Vector3f( rotate, 0, 0 );
        rotate += 0.01f;
        transformation.updateModel( translation, 1.0f, rotation );
        shader.set( "modelMatrix", transformation.getModelMatrix() );

        glDrawElements( GL_TRIANGLES, quads.size() * 6, GL_UNSIGNED_INT, 0 );
    }

    private void init() {
        VAO = glGenVertexArrays();
        glBindVertexArray( VAO );

        VBO = glGenBuffers();
        glBindBuffer( GL_ARRAY_BUFFER, VBO );
        glBufferData( GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW );

        EBO = glGenBuffers();
        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, EBO );
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW );

        glVertexAttribPointer( 0, 3, GL_FLOAT , false, 5 * Float.BYTES, 0 );
        glEnableVertexAttribArray( 0 );

        glVertexAttribPointer( 1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES );
        glEnableVertexAttribArray( 1 );

        MemoryUtil.memFree( vertices );
    }

    private static final float Z = -2.0f;
    private void generateVertices() {
        vertices = MemoryUtil.memAllocFloat( quads.size() * 20 );

        for ( int i = 0; i < quads.size(); i++ ) {
            Quad quad = quads.get( i );

            float xMin = ( float ) quad.x / SCREEN_WIDTH * 2.0f - 1.0f;
            float xMax = ( float ) ( quad.x + quad.width ) / SCREEN_WIDTH * 2.0f - 1.0f;
            float yMin = 1.0f - ( float ) quad.y / SCREEN_HEIGHT * 2.0f;
            float yMax = 1.0f - ( float ) ( quad.y + quad.height ) / SCREEN_HEIGHT * 2.0f;

            float xMinA = ( float ) quad.uvX / texture.width;
            float xMaxA = ( float ) ( quad.uvX + quad.width ) / texture.width;
            float yMinA = 1.0f - ( float ) quad.uvY / texture.height;
            float yMaxA = 1.0f - ( float ) ( quad.uvY + quad.height ) / texture.height;

            //vertices.put( xMin ).put( yMin ).put( Z ).put( xMinA ).put( yMinA );
            //vertices.put( xMax ).put( yMin ).put( Z ).put( xMaxA ).put( yMinA );
            //vertices.put( xMax ).put( yMax ).put( Z ).put( xMaxA ).put( yMaxA );
            //vertices.put( xMin ).put( yMax ).put( Z ).put( xMinA ).put( yMaxA );
            vertices.put( -1.0f ).put( -1.0f ).put( Z ).put( 0.0f ).put( 0.0f );
            vertices.put( 1.0f ).put( -1.0f ).put( Z ).put( 1.0f ).put( 0.0f );
            vertices.put( 1.0f ).put( 1.0f ).put( Z ).put( 1.0f ).put( 1.0f );
            vertices.put( -1.0f ).put( 1.0f ).put( Z ).put( 0.0f ).put( 1.0f );

        }

        vertices.flip();
    }

    private void generateIndices() {
        indices = new int[ quads.size() * 6 ];
        for ( int i = 0; i < quads.size(); i++ ) {
            int vertexIndex = i * 4;
            int indexOffset = i * 6;

            indices[ indexOffset ] = vertexIndex;
            indices[ indexOffset + 1 ] = vertexIndex + 1;
            indices[ indexOffset + 2 ] = vertexIndex + 2;
            indices[ indexOffset + 3 ] = vertexIndex + 2;
            indices[ indexOffset + 4 ] = vertexIndex + 3;
            indices[ indexOffset + 5 ] = vertexIndex;
        }
    }
}
