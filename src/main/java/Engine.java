import utils.Time;

public class Engine {

    private final GlfwWindow win;
    private final Render render;
    private final Time time;

    public Engine( GlfwWindow win ) {
        this.win = win;
        render = new Render( win );
        time = new Time();
    }

    public void run() {
        while ( !win.shouldClose() ) {
            time.start();

            inputHandler();

            render.render();

            win.setTitle( time.getFps() + " FPS"  );

            time.end();
        }
        win.terminate();
    }

    private void inputHandler() {
        //todo
    }
}
