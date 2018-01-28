package model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {
    @Test
    public void testLike() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        assertFalse(photo.getFavourite());
        photo.like();
        assertTrue(photo.getFavourite());
    }

    @Test
    public void testUnlike() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        assertFalse(photo.getFavourite());
        photo.like();
        photo.unlike();
        assertFalse(photo.getFavourite());
    }

    @Test
    public void testGetFileName() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        Photo photo2 = new Photo("test" + File.separator + "thisisimage.png");
        assertEquals("smile.png", photo.getFileName());
        assertEquals("thisisimage.png", photo2.getFileName());
    }

    @Test
    public void testAddTag() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("hi"));
        photo.addTag(tags);
        assertEquals(1, photo.getCurTags().size());
        photo.removeTag(tags);
    }

    @Test
    public void testRemoveTag() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        ArrayList<Tag> tags = new ArrayList<>();
        ArrayList<Tag> remove = new ArrayList<>();
        ArrayList<Tag> remaining = new ArrayList<>();
        assertEquals(0, photo.getCurTags().size());
        Tag t1 = new Tag("hi");
        Tag t2 = new Tag("bye");
        Tag t3 = new Tag("hello");
        tags.add(t1);
        tags.add(t2);
        tags.add(t3);
        photo.addTag(tags);
        remove.add(t1);
        remaining.add(t2);
        remaining.add(t3);
        photo.removeTag(remove);
        assertEquals(2, photo.getCurTags().size());
        photo.removeTag(remaining);
    }

    @Test
    public void testChangeToOldTag() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("bye"));
        tags.add(new Tag("hello"));
        photo.addTag(tags);
        String date = photo.getTimes().get(0);
        assertEquals("smile @bye @hello.png", photo.getFileName());
        photo.changeToOldTag(date);
        assertEquals("smile.png", photo.getFileName());
    }

    @Test
    public void restRename() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        photo.rename("newname");
        assertEquals("newname", photo.getName());
        photo.rename("smile");
        assertEquals("smile", photo.getName());
    }

    @Test
    public void testMove() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        assertEquals("test"+ File.separator + "smile.png", photo.getPath());
        photo.move("test" + File.separator + "folder1");
        assertEquals("test" + File.separator + "folder1" + File.separator + "smile.png", photo.getPath());
        photo.move("test");
    }

    @Test
    public void testGetTimes() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        assertNotNull(photo.getTimes());
    }

    @Test
    public void testGetCurTags() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("hi"));
        tags.add(new Tag("bye"));
        tags.add(new Tag("hello"));
        photo.addTag(tags);
        assertEquals(3, photo.getCurTags().size());
        photo.removeTag(tags);
        assertEquals(0, photo.getCurTags().size());
    }

    @Test
    public void testGetFile() throws IOException{
        Photo photo1 = new Photo("test" + File.separator + "smile.png");
        Photo photo2 = new Photo("test" + File.separator + "folder1" + File.separator + "mostInside.png");
        assertEquals("test" + File.separator + "smile.png", photo1.getFile().getPath());
        assertEquals("test" + File.separator + "folder1" + File.separator + "mostInside.png", photo2.getFile().getPath());
    }

    @Test
    public void testGetPrevName() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        photo.rename("newName");
        assertEquals("newName", photo.getName());
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        assertEquals("smile.png", photo.getPrevName(df.format(new Date())));
    }

    @Test
    public void testGetPath() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        assertEquals("test" + File.separator + "smile.png", photo.getPath());
    }

    @Test
    public void testEquals() throws IOException{
        Photo photo = new Photo("test" + File.separator + "smile.png");
        Photo samePhoto = new Photo("test" + File.separator + "smile.png");
        Photo different = new Photo("test" + File.separator + "thisisimage.png");
        assertTrue(photo.equals(samePhoto));
        assertFalse(photo.equals(different));
    }

    @Test
    public void testGetName() throws IOException{
        Photo photo1 = new Photo("testing" + File.separator + "smile.png");
        Photo photo2 = new Photo("testing" + File.separator + "folder1" + File.separator + "mostInside.png");
        assertEquals("smile", photo1.getName());
        assertEquals("mostInside", photo2.getName());
    }

    @Test
    public void testGetFavourite() throws IOException{
        Photo photo = new Photo("testing" + File.separator + "smile.png");
        assertFalse(photo.getFavourite());
        photo.like();
        assertTrue(photo.getFavourite());
    }

}