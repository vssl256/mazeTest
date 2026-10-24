import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL30C.*;

public class TextMesh {
    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_HEIGHT = 1080;

    private final Font font;

    List<Glyph> glyphs;
    private int[] indices;
    private FloatBuffer vertices;

    private int VAO;
    private int VBO;
    private int EBO;

    public TextMesh( int x, int y, String string, Font font ) {
        this.font = font;
        glyphs = font.createGlyphs( x, y, string );

        generateIndices();
        generateVertices();
        init();
    }

    public void draw() {
        glUseProgram( font.shader.id );
        glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_2D, font.atlas.id );
        glBindVertexArray( VAO );
        glDrawElements( GL_TRIANGLES, glyphs.size() * 6, GL_UNSIGNED_INT, 0 );
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

    private void generateVertices() {
        vertices = MemoryUtil.memAllocFloat( glyphs.size() * 20 );

        for ( int i = 0; i < glyphs.size(); i++ ) {
            Glyph glyph = glyphs.get( i );

            float xMin = ( float ) glyph.x / SCREEN_WIDTH * font.size * 2.0f - 1.0f;
            float xMax = ( float ) ( glyph.x + glyph.width ) / SCREEN_WIDTH * font.size * 2.0f - 1.0f;
            float yMin = 1.0f - ( float ) glyph.y / SCREEN_HEIGHT * font.size * 2.0f;
            float yMax = 1.0f - ( float ) ( glyph.y + glyph.height ) / SCREEN_HEIGHT * font.size * 2.0f;

            float xMinA = ( float ) glyph.atlasX / font.scaleW;
            float xMaxA = ( float ) ( glyph.atlasX + glyph.width ) / font.scaleW;
            float yMinA = 1.0f - ( float ) glyph.atlasY / font.scaleH;
            float yMaxA = 1.0f - ( float ) ( glyph.atlasY + glyph.height ) / font.scaleH;

            vertices.put( xMin ).put( yMin ).put( 0.0f ).put( xMinA ).put( yMinA );
            vertices.put( xMax ).put( yMin ).put( 0.0f ).put( xMaxA ).put( yMinA );
            vertices.put( xMax ).put( yMax ).put( 0.0f ).put( xMaxA ).put( yMaxA );
            vertices.put( xMin ).put( yMax ).put( 0.0f ).put( xMinA ).put( yMaxA );
        }

        vertices.flip();
    }

    private void generateIndices() {
        indices = new int[ glyphs.size() * 6 ];
        for ( int i = 0; i < glyphs.size(); i++ ) {
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
