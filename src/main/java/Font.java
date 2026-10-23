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
    public final Shader shader;

    public Font( String fontName, Shader shader ) {
        this.name = fontName;
        this.shader = shader;

        initGlyphs();
        initAtlas();
    }

    private void initAtlas() {
        atlas = new Texture( name, png, FONT );
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

    public FontData.CharData createGlyph( char c ) {
        return charMap.get( ( int ) c );
    }

    public List<FontData.CharData> createGlyphs( String string) {
        List<FontData.CharData> glyphs = new ArrayList<>();
        for ( char glyph : string.toCharArray() ) {
            FontData.CharData charData = charMap.get( ( int ) glyph );
            glyphs.add( charData );
        }
        return glyphs;
    }

    public List<Quad> createText( int x, int y, String string, float size ) {
        List<Quad> glyphs = new ArrayList<>();
        for ( char glyph : string.toCharArray() ) {
            FontData.CharData charData = charMap.get( ( int ) glyph );
            if ( glyph == '\n' ) { x = 0; y += lineHeight; }
            Quad character = createGlyph( x, y, ( int ) glyph, size );
            if ( glyph != '\n' ) x += charData.xadvance;
            glyphs.add( character );
        }
        return glyphs;
    }

    public Quad createQuad( int x, int y, FontData.CharData glyph, float size ) {
        return new Quad( x, y, glyph.width, glyph.height, shader, atlas, glyph.x + glyph.xoffset, glyph.y + glyph.yoffset, scaleW, scaleH, size );
    }

    public List<Quad> createLabel( int x, int y, String string, float size ) {
        List<Quad> glyphs = new ArrayList<>();

        for ( char glyph : string.toCharArray() ) {
            FontData.CharData charData = charMap.get( ( int ) glyph );

            boolean newLine = glyph == '\n' || x + charData.width >= 1920;
            if ( newLine ) {
                y += lineHeight;
                x = 0;
            } else {
                Quad character = createGlyph( x, y, ( int ) glyph, size );
                x += charData.xadvance;
                glyphs.add( character );
            }
        }

        return glyphs;
    }

    public List<Quad> createQuads( int x, int y, List<FontData.CharData> charData, float size ) {
        List<Quad> quads = new ArrayList<>();
        for ( FontData.CharData glyph : charData ) {
            Quad quad = new Quad( x, y, glyph.width, glyph.height, shader, atlas, glyph.x + glyph.xoffset, glyph.y + glyph.yoffset, scaleW, scaleH, size );
            quads.add( quad );
        }
        return quads;
    }

    public Quad createGlyph(int x, int y, Integer id, float size ) {
        if ( id == ( int ) '\n' ) id = 32;
        FontData.CharData charData = charMap.get( id );
        return new Quad( x, y, charData.width, charData.height, shader, atlas, charData.x + charData.xoffset, charData.y + charData.yoffset, scaleW, scaleH, size );
    }
}
