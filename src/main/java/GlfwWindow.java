import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GlfwWindow {

    public long id;
    public int width, height;
    public String title;

    public boolean isFullscreen = false;
    public boolean isResized = false;
    private boolean isVsync = true;

    private GLFWVidMode mode;
    private long monitor;

    public boolean[] keys = new boolean[ GLFW_KEY_LAST - 1 ];

    private final float[] clearColor = { 0, 0, 0 };

    public GlfwWindow( int width, int height, String title ) {
        this.width = width;
        this.height = height;
        this.title = title;

        init();
    }

    public void setTitle( String title ) {
        glfwSetWindowTitle( id, title );
    }

    private void init() {
        glfwInit();

        debugPlatform();

        setWindowHints();

        id = glfwCreateWindow( width, height, title, NULL, NULL );
        if ( id == NULL ) {
            System.err.println( "Failed to create GLFW window" );
            glfwTerminate();
        }

        glfwMakeContextCurrent( id );

        glfwSwapInterval( isVsync ? 1 : 0 );

        GL.createCapabilities();
        
        glEnable( GL_BLEND );
        glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );

        initMonitor();

        setCallbacks();
    }

    private void debugPlatform() {
        int platform = glfwGetPlatform();
        switch ( platform ) {
            case GLFW_PLATFORM_WAYLAND -> System.out.println( "Wayland" );
            case GLFW_PLATFORM_X11 -> System.out.println( "X11" );
            case GLFW_PLATFORM_WIN32 -> System.out.println( "Win32" );
            case GLFW_PLATFORM_COCOA -> System.out.println( "Cocoa" );
            default -> System.out.println( "Unrecognised platform" );
        }
    }

    private void initMonitor() {
        PointerBuffer monitors = glfwGetMonitors();
        while ( monitors != null && monitors.hasRemaining() ) {
            monitor = monitors.get();
        }
        mode = glfwGetVideoMode( monitor );
    }

    private void setCallbacks() {
        glfwSetFramebufferSizeCallback( id, this::FramebufferSizeCallback );
        glfwSetKeyCallback( id, this::keyCallback );
    }

    private void FramebufferSizeCallback( long _win, int frameBufferWidth, int frameBufferHeight ) {
        isResized = true;
        glViewport( 0, 0, frameBufferWidth, frameBufferHeight );
        this.width = frameBufferWidth;
        this.height = frameBufferHeight;
    }

    private void keyCallback( long _win, int key, int _scancode, int action, int _mods ) {
        if ( key >= 0 && key <= GLFW_KEY_LAST ) {
            keys[ key ] = ( action != GLFW_RELEASE );
        }
        if ( action == GLFW_PRESS ) {
            switch ( key ) {
                case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose( id, true );
                case GLFW_KEY_V -> toggleVsync();
                case GLFW_KEY_P -> togglePolygonMode();
                case GLFW_KEY_F -> toggleFullscreen();
            }
        }
    }

    private void toggleFullscreen() {
        if ( !isFullscreen ) {
            glfwSetWindowMonitor( id, monitor, 0, 0, mode.width(), mode.height(), mode.refreshRate() );
        } else {
            glfwSetWindowMonitor( id, NULL, 0, 0, width, height, mode.refreshRate() );
        }
        isFullscreen = !isFullscreen;
    }

    private int currentPolygonMode = GL_FILL;
    private void togglePolygonMode() {
        currentPolygonMode = ( currentPolygonMode == GL_FILL ) ? GL_LINE : GL_FILL;
        glPolygonMode( GL_FRONT_AND_BACK, currentPolygonMode );
    }

    private void toggleVsync() {
        isVsync = !isVsync;
        glfwSwapInterval( isVsync ? 1 : 0 );
    }

    public void clear() {
        glClearColor( clearColor[ 0 ], clearColor[ 1 ], clearColor[ 2 ], 1f );
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose( id );
    }

    public void swapBuffers() {
        glfwSwapBuffers( id );
        glfwPollEvents();
    }

    public void terminate() {
        glfwFreeCallbacks( id );
        glfwDestroyWindow( id );
        glfwTerminate();
    }

    private void setWindowHints() {
        glfwDefaultWindowHints();
        glfwWindowHint( GLFW_CONTEXT_VERSION_MAJOR, 3 );
        glfwWindowHint( GLFW_CONTEXT_VERSION_MINOR, 3 );
        glfwWindowHint( GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE );
        glfwWindowHint( GLFW_REFRESH_RATE, 180 );
        glfwWindowHint( GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE );
    }
}

