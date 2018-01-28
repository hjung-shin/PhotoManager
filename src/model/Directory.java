package model;

import java.io.File;
import java.util.ArrayList;

public class Directory {
  /** ArrayList of Photos inside this Directory. */
  private ArrayList<Photo> photos;
  /** The name of this Directory. */
  private String name;
  /** The file of this Directory. */
  private File file;

  /**
   * Creates a new Directory instance from a File object.
   *
   * @param file The File object this new Directory is based on.
   */
  protected Directory(File file) {
    this.photos = new ArrayList<>();
    this.file = file;
    String path = file.getPath();
    this.name = path.substring(path.lastIndexOf(File.separator) + 1);
  }

  /**
   * Checks if there exists a photo in this Directory that has the same name.
   * Returns true or false depending on if there is a photo with the same name or no photo with the same name respectively.
   *
   * @param name    The name to compare and check if there exist another photo with the same name.
   * @return    If there exist a photo with the same name in this Directory.
   */
  boolean checkSameName(String name) {
    for (Photo photo : this.photos) {
      if (photo.getName().trim().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the ArrayList of Photos in this Directory. If there is no photos in this Directory, empty ArrayList will be returned.
   *
   * @return ArrayList  ArrayList of Photos that are contained in this Directory.
   */
  public ArrayList<Photo> getPhotos() {
    return this.photos;
  }

  /**
   * Adds a photo to this Directory. Adds a the photo in this Directory's ArrayList of Photos.
   *
   * @param photo The new photo that needs to be added in this ArrayList of Photos.
   */
  void addPhoto(Photo photo) {
    this.photos.add(photo);
  }

  /**
   * Returns the name of this Directory.
   *
   * @return The name of this Directory.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the File of this Directory
   *
   * @return    File object of this Directory.
   */
  public File getFile() {
    return this.file;
  }
}
