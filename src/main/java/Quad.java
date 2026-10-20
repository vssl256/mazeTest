import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Quad {
    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 480;

    private final Shader shader;
    private final Texture texture;

    private FloatBuffer verticesBuffer;
    private int VAO;
    private int VBO;
    private int EBO;

    private final int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    public Quad( float[] vertices, Shader shader, Texture texture ) {
        this.shader = shader;
        this.texture = texture;

        verticesBuffer = MemoryUtil.memCallocFloat( vertices.length );
        verticesBuffer.put( 0, vertices );

        init();
    }

    public Quad( int x, int y, int width, int height, Shader shader, Texture texture, int atlasX, int atlasY, int atlasW, int atlasH, int size ) {
        float xMin = ( float ) x / SCREEN_WIDTH * size * 2.0f - 1.0f;
        float xMax = ( float ) ( x + width ) / SCREEN_WIDTH * size * 2.0f - 1.0f;
        float yMin = 1.0f - ( float ) y / SCREEN_HEIGHT * size * 2.0f;
        float yMax = 1.0f - ( float ) ( y + height ) / SCREEN_HEIGHT * size * 2.0f;

        float xMinA = ( float ) atlasX / atlasW;
        float xMaxA = ( float ) ( atlasX + width ) / atlasW;
        float yMinA = 1.0f - ( float ) atlasY / atlasH;
        float yMaxA = 1.0f - ( float ) ( atlasY + height ) / atlasH;

        float[] vertices = {
                xMin, yMin, 0.0f,     xMinA, yMinA,
                xMax, yMin, 0.0f,     xMaxA, yMinA,
                xMax, yMax, 0.0f,     xMaxA, yMaxA,
                xMin, yMax, 0.0f,     xMinA, yMaxA
        };

        this.shader = shader;
        this.texture = texture;

        verticesBuffer = MemoryUtil.memCallocFloat( vertices.length );
        verticesBuffer.put( 0, vertices );

        init();
    }

    private void init() {
        VAO = glGenVertexArrays();
        glBindVertexArray( VAO );

        VBO = glGenBuffers();
        glBindBuffer( GL_ARRAY_BUFFER, VBO );
        glBufferData( GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW );

        EBO = glGenBuffers();
        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, EBO );
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW );

        glVertexAttribPointer( 0, 3, GL_FLOAT , false, 5 * Float.BYTES, 0 );
        glEnableVertexAttribArray( 0 );

        glVertexAttribPointer( 1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES );
        glEnableVertexAttribArray( 1 );

        MemoryUtil.memFree( verticesBuffer );
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
    }

    public void draw() {
        shader.recompile();
        glUseProgram( shader.id );

        glActiveTexture( GL_TEXTURE );
        if ( texture != null ) {
            glBindTexture( GL_TEXTURE_2D, texture.id );
        } else {
            glBindTexture( GL_TEXTURE_2D, 0 );
        }

        glBindVertexArray( VAO );
        glDrawElements( GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0 );
        glUseProgram( 0 );
    }
}
