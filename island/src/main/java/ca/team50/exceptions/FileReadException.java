package ca.team50.exceptions;

import java.io.IOException;

// Exception class for failing to read file
public class FileReadException extends IOException {

    private String invalidPath;

    public FileReadException(String path) {
        super("Invalid Path: " + path);
        this.invalidPath = path;
    }


    public String getInvalidPath() {
        return this.invalidPath;
    }

}
