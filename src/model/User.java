package model;

import java.io.File;
import java.util.ArrayList;

// a class to keep track of the current photo.(used when moving from different screens in frontend)
public class User {

  /** current photo */
  private static Photo curPhoto = null;

  /** The Photos being used in the screen. */
  private static ArrayList<Photo> galleryPhotos;

  /** The root directory of this file */
  private static File rootDir;

  /**
   * Select photo to edit the photo.
   *
   * @param photo The photo selected by the user.
   */
  public static void setPhoto(Photo photo) {
    if (photo != null) {
      curPhoto = PhotoManager.getPhoto(photo.getPath());
    }
  }

  /**
   * getter for this curPhoto
   *
   * @return curPhoto
   */
  public static Photo getPhoto() {
    return curPhoto;
  }

  /**
   * Getter for rootDir.
   *
   * @return root directory the user set.
   */
  public static File getRootDir() {
    return rootDir;
  }

  /**
   * Setter for rootDir.
   *
   * @param dir directory file that the user want to set as root directory.
   */
  public static void setRootDir(File dir) {
    rootDir = dir;
  }

  /**
   * Getter for galleryPhotos
   *
   * @return ArrayList of photos that the user wants to display in galleryDisplay.
   */
  public static ArrayList<Photo> getGalleryPhotos() {
    return galleryPhotos;
  }

  /**
   * the setter for galleryPhotos.
   *
   * @param galleryPhotos the Photos to be set to.
   */
  public static void setGalleryPhotos(ArrayList<Photo> galleryPhotos) {
    User.galleryPhotos = galleryPhotos;
  }
}
