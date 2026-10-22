public class Main {

    void main() {
        GlfwWindow win = new GlfwWindow( 1920/3, 1080/3, "GLFW Window" );
        Engine engine = new Engine( win );
        engine.run();
    }
}
