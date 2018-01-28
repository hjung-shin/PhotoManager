package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Photo class that saves the instances of the each photo file. It also edits its tags and its
 * paths.
 *
 * <p>2017-11-30
 */
public class Photo implements Serializable {

  /** Name of this photo including the file type */
  private String fileName;

  /** Name of this photo without the file type */
  private String name;

  /** The file for this Photo object. */
  public File file;

  /** Indicator for the favourited picture. */
  private boolean favourite = false;

  /**
   * All the past names of this Photo are stored in this array list. The index of the array list
   * indicates the time of the same index in the time array list.
   */
  private ArrayList<String> names = new ArrayList<>();

  /** ArrayList of current tags of this Photo. */
  private ArrayList<Tag> curTags = new ArrayList<>();

  /** The ArrayList of times when this Photo was edited */
  private ArrayList<String> times = new ArrayList<>();

  /** The date objects. */
  private DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

  /**
   * Constructor.
   *
   * @param file the file of this Photo object. Every Photo has a unique file path that
   *     differentiates from each other.
   */
  public Photo(String file) throws IOException {
    this.file = new File(file);
    this.fileName = this.file.getName();
    // some Photos may already have a tag, so filter it.
    filter();
    Date dateObj = new Date();
    times.add(df.format(dateObj));
    names.add(this.fileName);
  }

  /**
   * initialize the Photo's name and if there are existing tags, add it to the curTag.
   *
   * @throws IOException On input error
   */
  private void filter() throws IOException {
    // if the Photo contains a tag.
    if (this.fileName.contains("@")) {
      int index1 = this.fileName.indexOf("@");
      this.name = this.fileName.substring(0, index1 - 1);
      String tags[] =
          this.fileName.substring(0, this.fileName.lastIndexOf(".")).split(" @");
      for (int i = 1; i < tags.length; i++) {
        Tag tag = new Tag((tags[i]));
        curTags.add(tag);
        TagManager.addTagInList(tag);
      }
    } else {
      int index2 = this.fileName.lastIndexOf(".");
      this.name = this.fileName.substring(0, index2);
    }
  }

  /**
   * Favourite this photo.
   */
  public void like() {
    this.favourite = true;
    try {
      PhotoManager.writeFile();
      PhotoManager.writeFilePastName();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Unfavourite this photo.
   */
  public void unlike() {
    this.favourite = false;
    try {
      PhotoManager.writeFile();
      PhotoManager.writeFilePastName();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * the getter of this fileName instance
   *
   * @return fileName
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * Add the tag into the ArrayList.
   *
   * @param taggedTags the list of tags to add to the Photo's file name.
   */
  public void addTag(ArrayList<Tag> taggedTags) {
    StringBuilder tagName = new StringBuilder();
    for (Tag tag : taggedTags) {
      curTags.add(tag);
      TagManager.addTagInList(tag);
      tagName.append(" " + tag.getName());
    }
    // the index to find the file type.
    int index = this.fileName.lastIndexOf(".");
    this.fileName =
        this.fileName.substring(0, index)
            + tagName.toString()
            + this.fileName.substring(index, this.fileName.length());

    // the index used to find the file's fileName.
    int index2 = this.file.getPath().lastIndexOf(File.separator);
    File newFile =
        new File(this.file.getPath().substring(0, index2 + 1) + this.fileName);

    // rename the file
    boolean success = this.file.renameTo(newFile);
    this.file = newFile;
    Date dateObj = new Date();

    // add time and new name into its ArrayList, this will keep them in the same index.
    times.add(df.format(dateObj));
    names.add(this.fileName);

    // an ArrayList that will have the time when its changed and the changed name.
    ArrayList<String> pastName = new ArrayList<>();
    pastName.add(df.format(dateObj));
    pastName.add(this.fileName);
    PhotoManager.allThePastNames.add(pastName);
    try {
      PhotoManager.writeFile();
      PhotoManager.writeFilePastName();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove the Tags in that is in the Photo object's instance (fileName, curTag)
   *
   * @param listToRemove list of Tags to remove from the Photo's file name
   * @throws IOException On input errors.
   */
  public void removeTag(ArrayList<Tag> listToRemove) throws IOException {
    // change the name by removing the tags.
    for (Tag tag : listToRemove) {
      curTags.remove(tag);
      int index = this.fileName.indexOf(tag.getName());
      this.fileName =
          this.fileName.substring(0, index - 1)
              + this.fileName.substring(index + tag.getName().length(),
              this.fileName.length());
    }

    // index used to find the file fileName
    int index = this.file.getPath().lastIndexOf(File.separator);
    File new_file = new File(this.file.getPath().substring(0, index + 1) +
        this.fileName);
    boolean success = this.file.renameTo(new_file);
    this.file = new_file;
    Date dateObj = new Date();

    // add time and new name into its ArrayList, this will keep them in the same index.
    names.add(this.fileName);
    times.add(df.format(dateObj));

    // an ArrayList that will have the time changed when the file name was changed and its pastName.
    ArrayList<String> pastName = new ArrayList<>();
    pastName.add(df.format(dateObj));
    pastName.add(this.fileName);
    PhotoManager.allThePastNames.add(pastName);
    try {
      PhotoManager.writeFile();
      PhotoManager.writeFilePastName();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * changes the current tag to the given time of the past tag.
   *
   * @param date the date of the old past name of this Photo to change into.
   */
  public void changeToOldTag(String date) {
    String old = getPrevName(date);
    this.fileName = old;
    // index to find the file name.
    int index2 = this.file.getPath().lastIndexOf((File.separator));
    File newFile = new File(this.file.getPath().substring(0, index2 + 1) +
        this.fileName);
    // rename the file.
    boolean success = this.file.renameTo(newFile);
    this.file = newFile;
    Date dateObj = new Date();

    // add time and new name into its ArrayList, this will keep them in the same index.
    times.add(df.format(dateObj));
    names.add(this.fileName);

    // if the old tag name has tags, add it to the curTags.
    ArrayList<Tag> theNewCurTag = new ArrayList<>();
    if (old.contains("@")) {
      int indexAt = old.indexOf("@");
      int indexDot = old.lastIndexOf(".");
      old = old.substring(indexAt + 1, indexDot);
      String[] tags = old.split(" @");
      for (String tag : tags) {
        theNewCurTag.add(new Tag(tag));
      }
    }
    curTags = theNewCurTag;

    // an ArrayList that will have the time changed when the file name was changed and its pastName.
    ArrayList<String> pastName = new ArrayList<>();
    pastName.add(df.format(dateObj));
    pastName.add(this.fileName);
    PhotoManager.allThePastNames.add(pastName);
    try {
      PhotoManager.writeFile();
      PhotoManager.writeFilePastName();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * rename the fileName of this photo.
   *
   * @param newName the newName for this Photo
   * @throws IOException On input error.
   */
  public void rename(String newName) throws IOException {
    // first check whether this Photo has a tag or not.
    if (this.fileName.contains("@")) {
      int index1 = this.fileName.indexOf("@");
      this.fileName = newName + " " + this.fileName.substring(index1, this.fileName.length());
    } else {
      int index1 = this.fileName.lastIndexOf(".");
      this.fileName = newName + this.fileName.substring(index1, this.fileName.length());
    }
    // rename the fileName and its name of this Photo.
    this.name = newName;
    int index2 = this.file.getPath().lastIndexOf(File.separator);
    File new_file = new File(this.file.getPath().substring(0, index2 + 1) +
        this.fileName);
    boolean success = this.file.renameTo(new_file);
    this.file = new_file;

    // add time and new name into its ArrayList, this will keep them in the same index.
    names.add(this.fileName);
    Date dateObj = new Date();
    times.add(df.format(dateObj));

    // an ArrayList that will have the time changed when the file name was changed and its pastName.
    ArrayList<String> pastName = new ArrayList<>();
    pastName.add(df.format(dateObj));
    pastName.add(this.fileName);
    PhotoManager.allThePastNames.add(pastName);
    try {
      PhotoManager.writeFile();
      PhotoManager.writeFilePastName();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * changes the path of this Photo
   *
   * @param newPath the new Path of this Photo.
   * @throws IOException On input error
   */
  public void move(String newPath) throws IOException {
    File newFile = new File(newPath + File.separator + this.fileName);
    boolean success = this.file.renameTo(newFile);
    this.file = newFile;
    // add the newly added Photo to the PhotoManger
    // PhotoManager.addPhoto(this);

  }

  /**
   * Getter for times
   *
   * @return Photo.times.
   */
  public ArrayList<String> getTimes() {
    return times;
  }

  /**
   * Getter for curTags
   *
   * @return photo.curTags
   */
  public ArrayList<Tag> getCurTags() {
    return curTags;
  }

  /**
   * Getter for file.
   *
   * @return Photo.file
   */
  public File getFile() {
    return this.file;
  }

  /**
   * getter for the specific fileName within the names ArrayList that corresponds to the time.
   *
   * @param time the time to find the past name of this Photo object.
   * @return past name
   */
  public String getPrevName(String time) {
    int index = times.indexOf(time);
    return names.get(index);
  }

  /**
   * Getter for filePath.
   *
   * @return Photo.file.getPath()
   */
  public String getPath() {
    return this.file.getPath();
  }

  /**
   * The equals method for this Class
   *
   * @param other the other object.
   * @return boolean whether they are equal or not.
   */
  public boolean equals(Object other) {
    if (other instanceof Photo) {
      Photo photo = (Photo) other;
      return this.getPath().equals(photo.getPath())
          && this.name.equals(photo.getName());
    } else return false;
  }

  /**
   * Getter for name.
   *
   * @return photo.name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter for favourite
   *
   * @return photo.favourite
   */
  public boolean getFavourite() {
    return favourite;
  }
}
