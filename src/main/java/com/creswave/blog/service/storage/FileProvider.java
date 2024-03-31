package com.creswave.blog.service.storage;

import java.io.File;
import java.io.IOException;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface FileProvider {

    Boolean isS3FileSystemProvider();


    String saveFile( File file );


    void downloadFile( String pathOrKey, File destinationFile ) throws IOException;


    File downloadFile( String pathOrKey );


    void deleteFile( String pathOrKey );
}
