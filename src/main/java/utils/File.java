package utils;

import java.io.FileReader;
import java.io.IOException;

public class File {
    private static final String RESOURCES_DIR = "src/main/resources/";
    private static final String SHADERS_DIR = RESOURCES_DIR + "shaders/";
    private static final String TEXTURES_DIR = RESOURCES_DIR + "textures/";

    private final java.io.File file;
    private final FileTypes fileType;
    private long lastModified;

    public File( FileTypes fileType , String name, FileExtensions extension ) {
        this.fileType = fileType;
        file = new java.io.File( getFileTypeDir( name ) + "." + extension.name() );
        lastModified = file.lastModified();
    }

    public String getPath() {
        return file.getPath();
    }

    public String read() {
        String output;
        try ( FileReader reader = new FileReader( file ) ) {
            output = reader.readAllAsString();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        return output;
    }

    public boolean isModified() {
        boolean modified = lastModified != file.lastModified();
        if ( modified ) lastModified = file.lastModified();
        return modified;
    }

    private String getFileTypeDir( String name ) {
        return switch ( fileType ) {
            case SHADER -> SHADERS_DIR + name + "/shader";
            case TEXTURE -> TEXTURES_DIR + name;
        };
    }
}
