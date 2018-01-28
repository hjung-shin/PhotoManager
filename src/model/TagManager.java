package model;

import java.io.*;
import java.util.ArrayList;

/**
 * This is a TagManager class  that take cares of the Tag objects
 * It stores and saves the Tag into a text file when the program is turned off.
 */
public class TagManager implements Serializable {
  /**
   * All the Tags
   */
  private static ArrayList<Tag> allTags = new ArrayList<>();

  /**
   * add tag to the all tags list.
   *
   * @param tag the Tag to be added into the arrayList of all tags.
   */
  public static void addTagInList(Tag tag) {
    //add only if it does not  contain the tag already.
    if (!allTags.contains(tag)) {
      allTags.add(tag);
      try {
        writeFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * remove tag to the all tags list.
   *
   * @param tag the tag to remove in the arrayList.
   */
  public static void removeTagInList(Tag tag) {
    if (allTags.contains(tag)) {
      // delete tag if the photo's current tags contain this tag
      for (Photo photo : PhotoManager.getPhotos()) {
        if (photo.getCurTags().contains(tag)) {
          ArrayList<Tag> deletedTag = new ArrayList<>();
          deletedTag.add(tag);
          try {
            photo.removeTag(deletedTag);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      allTags.remove(tag);
      try {
        writeFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Write the file according the array list.
   * It is saved into a hidden txt file.
   *
   * @throws IOException On input errors
   */
  private static void writeFile() throws IOException {
    File f = new File(".tags.txt");
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    oos.writeObject(allTags);
    oos.flush();
    oos.close();
  }
  /**
   * Read the file and photos equal to the ArrayList in the photos.txt file and assign it to
   * the arrayList.
   *
   * @throws IOException ON input errors
   * @throws ClassNotFoundException On classpath errors.
   */
  public static void readFile() throws IOException, ClassNotFoundException {
    File f = new File(".tags.txt");
    if (f.exists()) {
      FileInputStream fis = new FileInputStream(".tags.txt");
      ObjectInputStream ois = new ObjectInputStream(fis);
      allTags = (ArrayList<Tag>) ois.readObject();
    }
  }

  /**
   * getter for allTags
   *
   * @return allTags
   */
  public static ArrayList<Tag> getTags() {
    return allTags;
  }
}
