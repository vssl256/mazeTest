import utils.Time;

import java.util.ArrayList;
import java.util.List;

import static utils.FileExtensions.*;
import static utils.FileTypes.*;

public class Engine {

    private final String string = "\n" +
            "\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse rutrum imperdiet ex, eu ultricies urna. In vehicula laoreet mauris, in bibendum mi fringilla vitae. Vivamus vel egestas massa. Nulla iaculis, lacus id posuere gravida, orci est ultrices mi, feugiat hendrerit massa ipsum in nisi. Donec quis sodales nisi. Cras facilisis rhoncus justo eget vulputate. Nullam ultricies orci ornare felis vulputate iaculis.\n" +
            "\n" +
            "Integer a dolor semper metus tempor varius quis a est. Duis vitae nibh sem. Donec nec metus non odio blandit accumsan. Praesent ultricies magna nec mauris congue, euismod lacinia nulla semper. Sed id neque lectus. Aenean dignissim aliquet mauris vel facilisis. Duis faucibus euismod lorem. Nunc lobortis turpis nec urna laoreet, ac tincidunt lorem laoreet. Morbi lobortis, massa eu sagittis vestibulum, diam turpis maximus enim, sit amet faucibus tortor ipsum at felis. Proin eleifend tristique interdum.\n" +
            "\n" +
            "Etiam bibendum porta massa, commodo venenatis tellus scelerisque ac. Suspendisse nec mollis turpis. Ut vulputate diam tempor erat venenatis placerat. Sed sagittis nibh sit amet dolor sagittis aliquet. Donec sed nulla ut purus malesuada accumsan eget eget tortor. Pellentesque feugiat posuere vulputate. Duis volutpat, ante ac accumsan facilisis, orci diam varius lectus, vitae eleifend mi magna eu velit. Pellentesque feugiat dolor in ipsum iaculis vehicula.\n" +
            "\n" +
            "Duis congue eros eu nulla rhoncus placerat. Integer a cursus orci. Quisque efficitur magna a pulvinar porta. Aenean malesuada mi ex, vel tempus velit volutpat vel. Nunc elit urna, rutrum sed faucibus vitae, consectetur eu ligula. Integer ut mi non nibh bibendum cursus. Donec in erat ex. Nullam aliquet ultrices eros, sed tincidunt turpis dignissim sed. In vestibulum consectetur tempus. Sed finibus ex ut libero elementum auctor. Donec quis erat aliquet, fringilla ex eu, interdum erat. Ut et tortor nec velit aliquet malesuada. Curabitur et euismod mauris. Etiam ultrices porttitor vehicula. Aliquam fermentum orci eget ex euismod porttitor.\n" +
            "\n" +
            "Praesent eu eros at purus vestibulum vulputate. Integer eu enim tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam luctus sem vel risus rutrum efficitur. Nulla sed arcu urna. In justo odio, euismod non quam sit amet, bibendum porttitor massa. Integer diam odio, mattis et rutrum ac, molestie gravida neque. Integer mattis blandit magna, vel interdum risus laoreet sit amet. Proin ut sagittis enim, nec facilisis tellus. In cursus, tortor et feugiat ullamcorper, dolor dui rhoncus ante, in posuere lorem elit sed nibh. Donec elit elit, facilisis sit amet ligula a, euismod congue elit. Mauris vitae ligula ullamcorper, fringilla ipsum a, blandit velit. Etiam porttitor neque eros, porttitor consequat mauris blandit at. Curabitur quis dolor nibh. Vestibulum convallis diam vel fermentum ornare. ";

    private final GlfwWindow win;
    private final Time time;
    private Render render;
    private List<Shader> loadedShaders = new ArrayList<>();
    private List<QuadDepr> quadDeprs = new ArrayList<>();
    private List<Label> labels = new ArrayList<>();
    private Label fpsLabel;

    public Engine( GlfwWindow win ) {
        this.win = win;
        initRender();
        time = new Time();
    }

    public void run() {
        while ( !win.shouldClose() ) {
            time.start();

            inputHandler();

            render.render();

            if ( time.timeElapsed > 1 ) {
                fpsLabel.set( time.getFps() + " FPS" );
                win.setTitle( time.getFps() + " FPS"  );
                time.timeElapsed = 0;

                loadedShaders.forEach( Shader::recompile );
            }

            time.end();
        }
        win.terminate();
    }

    private void initRender() {
        Transformation transformation = new Transformation(
                70.0f,
                1.0f,
                0.01f,
                1000.0f
        );

        Shader quadShader = new Shader( "default" );
        Texture quadTexture = new Texture( "test", png, TEXTURE );

        Shader fontShader = new Shader( "font1" );
        Font font = new Font( "test", fontShader, 1.0f );
        fpsLabel = new Label( font, "a", 0, 0 );
        Label label = new Label( font, string, 0, 30 );
        labels.add( fpsLabel );
        labels.add( label );

        loadedShaders.add( quadShader );
        loadedShaders.add( fontShader );

        List<QuadMesh> quadMeshes = new ArrayList<>();
        List<Quad> quads = new ArrayList<>();

        for ( int i = 0; i < 1920 / 50; i++ ) {
            Quad quad1 = new Quad( i * 50, 0 , i * 50, 0, quadTexture.width, quadTexture.height );
            quads.add( quad1 );
        }

        QuadMesh quadMesh = new QuadMesh( quads, quadTexture, quadShader, transformation );
        quadMeshes.add( quadMesh );


        render = new Render( win, transformation, quadMeshes, labels );
    }

    private void inputHandler() {
        //todo
    }
}
