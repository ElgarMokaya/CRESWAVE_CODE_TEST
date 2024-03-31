package com.creswave.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.creswave.blog.service.storage.FileProvider;
import com.creswave.blog.service.storage.FileProviderEnum;
import com.creswave.blog.service.storage.FileSystemFileProvider;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Configuration
public class FileProviderConfig {

    @Value( "${file.provider}" )
    private String fileProvider;

    @Bean
    FileProvider createFileProvider() {

        FileProviderEnum fileProviderEnum = FileProviderEnum.valueOf( fileProvider );
        FileProvider fileProvider = null;

        switch ( fileProviderEnum ) {

            case FILE_SYSTEM_FILE_PROVIDER:
                fileProvider = new FileSystemFileProvider();
                break;
        }

        return fileProvider;
    }
}
