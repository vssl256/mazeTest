import java.util.List;

public class FontData {

    public Common common;
    public List<CharData> chars;

    public static class Common {
        public int scaleW;
        public int scaleH;
        public int lineHeight;
    }

    public static class CharData {
        public int id;
        public int x;
        public int y;
        public int width;
        public int height;
        public int xadvance;
    }
}
