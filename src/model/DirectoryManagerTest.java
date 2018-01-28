package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DirectoryManagerTest {

    @Test
    public void testDirectoryManager() throws IOException{
        DirectoryManager.set(new File("test"));
        assertNotNull(DirectoryManager.getTree());
    }

    @Test
    public void testTree() throws IOException{
        DirectoryManager.set(new File("test"));
        Directory d = DirectoryManager.getTree().getDir();
        assertEquals("test", d.getName());
    }

    @Test
    public void testTreeChildren() throws IOException{
        DirectoryManager.set(new File("test"));
        ArrayList<Node<Directory>> d = DirectoryManager.getTree().getChildren();
        assertEquals(1, d.size());
        assertEquals("folder1", d.get(0).getDir().getName());
    }

    @Test
    public void testGetPhotos() throws IOException{
        DirectoryManager.set(new File("test"));
        ArrayList<Photo> photos = DirectoryManager.getTree().getDir().getPhotos();
        assertEquals(2, photos.size());

    }

    @Test
    public void testGetFile() throws IOException{
        DirectoryManager.set(new File("test"));
        Directory top = DirectoryManager.getTree().getDir();
        Directory under = DirectoryManager.getTree().getChildren().get(0).getDir();
        assertEquals("test", top.getFile().getPath());
        assertEquals("test" + File.separator + "folder1", under.getFile().getPath());
    }

    @Test
    public void testCheckSameName() throws IOException{
        DirectoryManager.set(new File("test"));
        Directory d = DirectoryManager.getTree().getDir();
        assertTrue(d.checkSameName("smile"));
        assertFalse(d.checkSameName("dontSmile"));
    }

    @Test
    public void testGetCurrentDirectoryByDir() throws IOException{
        DirectoryManager.set(new File("test"));
        Directory d = DirectoryManager.getCurrentDirectory("test" + File.separator + "folder1");
        assertEquals("folder1", d.getName());

    }

    @Test
    public void testCheckSamePicture()throws IOException{
        DirectoryManager.set(new File("test"));
        Photo p = DirectoryManager.getTree().getChildren().get(0).getDir().getPhotos().get(0);
        assertTrue(DirectoryManager.checkSamePicture(p, "mostInside"));
        assertFalse(DirectoryManager.checkSamePicture(p, "hi"));
    }
}