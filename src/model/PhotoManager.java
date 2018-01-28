package model;

import java.io.*;
import java.util.ArrayList;

/**
 * This is a PhotoManager class. It saves all the Photo objects manges them.
 *
 * <p>2017-11-30
 */
public class PhotoManager implements Serializable {

  /** ArrayList that keep track of all the photos. */
  private static ArrayList<Photo> photos = new ArrayList<>();

  /**
   * ArrayList that keep track of all the past names of the photo and its date
   * Ex) ((Date, Name), ..., (Date, Name))
   */
  static ArrayList<ArrayList<String>> allThePastNames = new ArrayList<>();

  /**
   * add photo to the ArrayList.
   *
   * @param photo photo to be added in photos(ArrayList with all Photo objects.)
   * @throws IOException On input error.
   */
  public static void addPhoto(Photo photo) throws IOException {
    photos.add(photo);
    writeFile();
  }

  /**
   * Write the ArrayList of Photo objects into the text file.
   *
   * @throws IOException On input error.
   */
  static void writeFile() throws IOException {
    File f = new File(".photos.txt");
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    // Write the ArrayList that is serialized into ".photos.txt"
    oos.writeObject(photos);
    oos.flush();
    oos.close();
  }

  /**
   * Read the file assign the photos object equal to the ArrayList stored in teh text file.
   *
   * @throws IOException On input error.
   * @throws ClassNotFoundException On classpath error.
   */
  public static void readFile() throws IOException, ClassNotFoundException {
    File f = new File(".photos.txt");
    if (f.exists()) {
      FileInputStream fis = new FileInputStream(f.getName());
      ObjectInputStream ois = new ObjectInputStream(fis);
      // assign photos to the objects stored in the text file.
      photos = (ArrayList<Photo>) ois.readObject();
    }
  }

  /**
   * Create a txt file that keeps track of the ArrayList that contains all the Photos' past name.
   *
   * @throws IOException On input error.
   */
  static void writeFilePastName() throws IOException {
    File f = new File(".log.txt");
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    oos.writeObject(allThePastNames);
    oos.flush();
    oos.close();
  }

  /**
   * Read the file and allThePastNames is assigned to the ArrayList in the text file.
   *
   * @throws IOException On input error.
   * @throws ClassNotFoundException On class path error.
   */
  public static void readFilePastName() throws IOException, ClassNotFoundException {
    File f = new File(".log.txt");
    if (f.exists()) {
      FileInputStream fis = new FileInputStream(f.getName());
      ObjectInputStream ois = new ObjectInputStream(fis);
      allThePastNames = (ArrayList<ArrayList<String>>) ois.readObject();
    }
  }

  /**
   * The getter of the photo that is find using the path.
   *
   * @param path the path of the Photo to be found.
   * @return Photo Photo object with the same path.
   */
  public static Photo getPhoto(String path) {
    for (Photo photo : photos) {
      // each paths are unique for each Photo object.
      if (path.equals(photo.getPath())) {
        return photo;
      }
    }
    return null;
  }

  /**
   * Get the whole list of Photo objects.
   *
   * @return ArrayList list of all Photo objects.
   */
  public static ArrayList<Photo> getPhotos() {
    return photos;
  }

  /**
   * Get the whole list of favourites photos.
   *
   * @return ArrayList
   */
  public static ArrayList<Photo> getFavouriteList() {
    ArrayList<Photo> favourites = new ArrayList<>();
    for (Photo photo : photos) {
      if (photo.getFavourite()) {
        favourites.add(photo);
      }
    }
    return favourites;
  }

  /**
   * Getter for allThePastNames
   *
   * @return PhotoManager.allThePastNames
   */
  public static ArrayList<ArrayList<String>> getAllThePastNames() {
    return allThePastNames;
  }
}
