import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import utils.File;
import utils.FileExtensions;
import utils.FileTypes;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL40C.*;
import static org.lwjgl.stb.STBImage.*;
import static utils.FileTypes.*;

public class Texture {

    public int id;
    public int width, height, channels;
    public File file;
    public FileTypes fileType;

    public Texture(String name, FileExtensions extension, FileTypes fileType ) {
        this.fileType = fileType;
        stbi_set_flip_vertically_on_load( true );

        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer bufferWidth = stack.mallocInt( 1 );
            IntBuffer bufferHeight = stack.mallocInt( 1 );
            IntBuffer bufferChannels = stack.mallocInt( 1 );

            file = new File( fileType, name, extension );
            ByteBuffer bufferTexture = stbi_load(
                    file.getPath(),
                    bufferWidth,
                    bufferHeight,
                    bufferChannels,
                    STBI_rgb_alpha
            );
            width = bufferWidth.get();
            height = bufferHeight.get();
            channels = bufferChannels.get();

            if ( bufferTexture != null ) {
                genTexture( bufferTexture );
                stbi_image_free( bufferTexture );
            } else {
                genMissingTexture();
                System.err.println( "Couldn't load texture " + name );
            }
        }
    }

    private void genTexture( ByteBuffer bufferTexture ) {
        id = glGenTextures();
        glBindTexture( GL_TEXTURE_2D, id );

        glPixelStorei( GL_UNPACK_ALIGNMENT, 1 );

        glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MIN_FILTER,
                GL_NEAREST
        );

        glTexParameteri(
                GL_TEXTURE_2D,
                GL_TEXTURE_MAG_FILTER,
                GL_NEAREST
        );

        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA8,
                width,
                height,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                bufferTexture
        );

        glGenerateMipmap( GL_TEXTURE_2D );
    }

    private void genMissingTexture() {
        int size = 2;
        int cellSize = 1;

        width = size;
        height = size;

        ByteBuffer bufferTexture = BufferUtils.createByteBuffer( size * size * 4 );

        for ( int y = 0; y < size; y++ ) {
            for ( int x = 0; x < size; x++ ) {
                if ( ( x / cellSize + y / cellSize ) % 2 == 0 ) {
                    bufferTexture.put( ( byte ) 255 );
                    bufferTexture.put( ( byte ) 0 );
                    bufferTexture.put( ( byte ) 255 );
                    bufferTexture.put( ( byte ) 0 );
                } else {
                    bufferTexture.put( ( byte ) 0 );
                    bufferTexture.put( ( byte ) 0 );
                    bufferTexture.put( ( byte ) 0 );
                    bufferTexture.put( ( byte ) 0 );
                }
            }
        }

        bufferTexture.flip();

        genTexture( bufferTexture );
    }
}
