import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import utils.File;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL40C.*;
import static utils.FileExtensions.frag;
import static utils.FileExtensions.vert;
import static utils.FileTypes.SHADER;

public class Shader {

    public int id;

    private final File vsFile;
    private final File fsFile;

    public Map<String, Integer> uLocations;
    public Map<String, Object> uniforms;

    public Shader( String name ) {
        vsFile = new File( SHADER, name, vert );
        fsFile = new File( SHADER, name, frag );

        uLocations = new HashMap<>();
        uniforms = new HashMap<>();

        id = compile();
        glUseProgram( id );
    }

    public void set( String name, Object value ) {
        if ( value instanceof Integer ) {
            set1i( name, ( int ) value );
            return;
        }
        if ( value instanceof Float ) {
            set1f( name, ( float ) value );
            return;
        }
        if ( value instanceof Double ) {
            set1d( name, ( double ) value );
            return;
        }
        if ( value instanceof float[] ) {
            set2f( name, ( float[] ) value );
            return;
        }
        if ( value instanceof Vector2f ) {
            set2fv( name, ( Vector2f ) value );
            return;
        }
        if ( value instanceof Vector3f ) {
            set3fv( name, ( Vector3f ) value );
            return;
        }
        if ( value instanceof Matrix4f ) {
            setMatrix4fv( name, ( Matrix4f ) value );
            return;
        }
        System.err.println( "Couldn't find Object type " + value.getClass().getName() );
    }

    private void setMatrix4fv( String name, Matrix4f matrix4fv ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, matrix4fv );
        }
        uniforms.replace( name, matrix4fv );
        setUniformMatrix4fv( name, matrix4fv );
    }

    private void set3fv( String name, Vector3f vector3f ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, vector3f );
        }
        uniforms.replace( name, vector3f );
        setUniform3fv( name, vector3f );
    }

    private void set2fv( String name, Vector2f vector2f ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, vector2f );
        }
        uniforms.replace( name, vector2f );
        setUniform2fv( name, vector2f );
    }

    private void set2f( String name, float[] value ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, value );
        }
        uniforms.replace( name, value );
        setUniform2f( name, value );
    }

    private void set1d( String name, double value ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, value );
        }
        uniforms.replace( name, value );
        setUniform1d( name, value );
    }

    private void set1f( String name, float value ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, value );
        }
        uniforms.replace( name, value );
        setUniform1f( name, value );
    }

    private void set1i( String name, int value ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, value );
        }
        uniforms.replace( name, value );
        setUniform1i( name, value );
    }

    private void createUniform( String name ) {
        int uLocation = glGetUniformLocation( id, name );
        if ( uLocation == -1 ) {
            uLocations.remove( name );
            uniforms.remove( name );
            return;
        }
        uLocations.put( name, uLocation );
    }

    private void setUniformMatrix4fv( String name, Matrix4f matrix4fv ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            FloatBuffer floatBuffer = stack.mallocFloat( 16 );
            glUniformMatrix4fv( uLocation, false, matrix4fv.get( floatBuffer ) );
        }
    }

    private void setUniform3fv( String name, Vector3f vector3f ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            FloatBuffer floatBuffer = stack.mallocFloat( 3 );
            glUniform3fv( uLocation, vector3f.get( floatBuffer ) );
        }
    }

    private void setUniform2fv( String name, Vector2f vector2f ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            FloatBuffer floatBuffer = stack.mallocFloat( 2 );
            glUniform2fv( uLocation, vector2f.get( floatBuffer ) );
        }
    }

    private void setUniform2f( String name, float[] value ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocations == null ) {
            return;
        }
        glUniform2fv( uLocation, value );
    }

    private void setUniform1d( String name, double value ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        glUniform1d( uLocation, value );
    }

    private void setUniform1f( String name, float value ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        glUniform1f( uLocation, value );
    }

    private void setUniform1i( String name, int value ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        glUniform1i( uLocation, value );
    }

    public void recompile() {
        if ( fsFile.isModified() || vsFile.isModified() ) {
            System.out.println( "Recompiling shaders..." );

            int newId = compile();
            if ( newId == -1 ) return;

            glDeleteProgram( id );
            id = newId;
            glUseProgram( id );

            setUniforms();
        }
    }

    private void setUniforms() {
        for ( Map.Entry<String, Object> entry : uniforms.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            set( key, value );
        }
    }

    private int compile() {
        int newId = glCreateProgram();

        String vss = vsFile.read();
        String fss = fsFile.read();

        int vertexShader = glCreateShader( GL_VERTEX_SHADER );
        glShaderSource( vertexShader, vss );
        glCompileShader( vertexShader );
        if ( glGetShaderi( vertexShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( vertexShader ) );
            glDeleteProgram( newId );
            glDeleteShader( vertexShader );
            return -1;
        }

        int fragmentShader = glCreateShader( GL_FRAGMENT_SHADER );
        glShaderSource( fragmentShader, fss );
        glCompileShader( fragmentShader );
        if ( glGetShaderi( fragmentShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( fragmentShader ) );
            glDeleteProgram( newId );
            glDeleteShader( fragmentShader );
            return -1;
        }

        glAttachShader( newId, vertexShader );
        glAttachShader( newId, fragmentShader );
        glLinkProgram( newId );
        glDeleteShader( vertexShader );
        glDeleteShader( fragmentShader );
        if ( glGetProgrami( newId, GL_LINK_STATUS ) == 0 ) {
            System.err.println( glGetProgramInfoLog( newId ) );
            glDeleteProgram( newId );
            return -1;
        }
        return newId;
    }

    public void debugMatrix( String name ) {
        int location = glGetUniformLocation( id, name );

        if ( location == -1 ) {
            System.out.println( name + ": uniform not found" );
            return;
        }

        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            FloatBuffer buffer = stack.mallocFloat( 16 );

            glGetUniformfv( id, location, buffer );

            float[] values = new float[ 16 ];

            for ( int i = 0; i < 16; i++ ) {
                values[ i ] = buffer.get( i );
            }

            System.out.println( name + ": " + Arrays.toString( values ) );
        }
    }
}