public class Main {

    void main() {
        GlfwWindow win = new GlfwWindow( 640, 480, "GLFW Window" );
        Engine engine = new Engine( win );
        engine.run();
    }
}
