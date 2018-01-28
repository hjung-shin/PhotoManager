package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectoryTest {

    @Test
    public void testDirectoryGetName() {
        File file = new File("test");
        Directory d = new Directory(file);
        assertEquals("test", d.getName());
    }

    @Test
    public void testDirectoryGetPath() {
        File file = new File("test");
        Directory d = new Directory(file);
        assertEquals("test", d.getFile().getPath());
    }
}