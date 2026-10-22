import com.google.gson.Gson;
import utils.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.FileTypes.FONT;
import static utils.FileExtensions.json;
import static utils.FileExtensions.png;

public class Font {
    public final String name;
    public Map<Integer, FontData.CharData> charMap = new HashMap<>();
    public Texture atlas;
    public int scaleW, scaleH;
    public int lineHeight;
    private final Shader shader;

    public Font( String fontName, Shader shader ) {
        this.name = fontName;
        this.shader = shader;

        initGlyphs();
        initAtlas();
    }

    private void initAtlas() {
        atlas = new Texture( "test", png, FONT );
    }

    private void initGlyphs() {
        File fontJson = new File( FONT, name, json );
        Gson gson = new Gson();
        FontData fontData = gson.fromJson( fontJson.read(), FontData.class );

        scaleW = fontData.common.scaleW;
        scaleH = fontData.common.scaleH;
        lineHeight = fontData.common.lineHeight;

        for ( FontData.CharData c : fontData.chars) {
            charMap.put( c.id, c );
        }
    }

    public List<Quad> createQuads( int x, int y, String string, int size ) {
        List<Quad> glyphs = new ArrayList<>();
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++ ) {
            char glyph = chars[i];
            FontData.CharData charData = charMap.get( ( int ) glyph );
            boolean newLine = ( glyph == '\n' );
            if ( !newLine ) { newLine = x + charData.width >= 1920; }
            if ( newLine ) {
                y += lineHeight;
                x = 0;
            } else {
                Quad character = drawCharacter( x, y, ( int ) glyph, size );
                x += charData.xadvance;
                glyphs.add( character );

            }
        }
        return glyphs;
    }

    public Quad drawCharacter( int x, int y, Integer id, int size ) {
        FontData.CharData charData = charMap.get( id );
        return new Quad( x, y, charData.width, charData.height, shader, atlas, charData.x, charData.y, scaleW, scaleH, size );
    }
}
