package com.creswave.blog.service.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public class FileSystemFileProvider implements FileProvider {

    @Override
    public Boolean isS3FileSystemProvider() {

        return false;
    }


    @Override
    public String saveFile( File file ) {

        return file.getAbsolutePath();
    }


    @Override
    public File downloadFile( String pathOrKey ) {

        File file = null;

        if ( pathOrKey != null ) {

            file = new File( pathOrKey );
        }

        return file;
    }


    @Override
    public void downloadFile( String pathOrKey, File destinationFile ) throws IOException {

        File sourceFile = new File( pathOrKey );
        Files.copy( sourceFile.toPath(), destinationFile.toPath() );
    }


    @Override
    public void deleteFile( String pathOrKey ) {

        File file = new File( pathOrKey );

        if ( file.exists() ) {

            file.delete();
        }
    }
}
