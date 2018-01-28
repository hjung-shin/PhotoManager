package model;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.regex.Pattern;

public class DirectoryManager {
  /** Root of the tree. */
  private static Node<Directory> tree;
  /** Default starting point of the node in the path. */
  private static int startIndex;
  /** Photo extensions. */
  private static final String[] photoExtension = {
    ".JPEG", ".JFIF", ".TIFF", ".GIF", ".BMP", ".PNG", ".JPG"
  };

  /**
   * Makes a new tree with the given File. Starts the formation of the tree with the top Node as the file. To build the tree, need to use loadFile to recurse through the directory.
   *
   * @param file    The root of the tree.
   * @throws IOException If the file does not exist.
   */
  public static void set(File file) throws IOException {
    tree = new Node<>(new Directory(file));
    String pattern = Pattern.quote(System.getProperty("file.separator"));
    startIndex = file.getPath().split(pattern).length;
    loadFile(file, tree);
  }

  /**
   * Traces into file and adds all the folders as Directories and as single nodes. Files with only photo extensions are added under the corresponding directory.
   *
   * @param file The file path to build.
   * @param current Node<Directory> to keep track of the current place.
   * @throws IOException Throws exception if file doesn't exist
   */
  private static void loadFile(File file, Node<Directory> current) throws IOException {
    File[] listFiles = file.listFiles();
    if (listFiles != null) {
      for (File f : listFiles) {
        if (f.isDirectory()) {
          current.addChildren(new Directory(f));
          loadFile(f, current.getChildren().get(current.getChildren().size() - 1));
        } else {
          for (String extension : photoExtension) {
            if (f.getPath().contains(".")) {
              if (f.getPath().substring(f.getPath().lastIndexOf(".")).equalsIgnoreCase(extension)) {
                Photo newPhoto = new Photo(f.getPath());
                // if this photo already exists PhotoManager.photos which is our database, then get
                // this photo
                if (PhotoManager.getPhotos().contains(newPhoto)) {
                  current.getDir().addPhoto(PhotoManager.getPhoto(newPhoto.getPath()));
                } else {
                  current.getDir().addPhoto(newPhoto);
                }
              }
            }
          }
        }
      }
    }
  }

    /**
     * Checks if there exists another photo with the same name as the newName under the same directory.
     * Returns true if there exists another photo with the same name with newName and returns false if there does not exists another photo with the same name.
     *
     * @param photo Photo that needs to be renamed.
     * @param newName Possible name to changed to, name to check.
     * @return  If there exists another photo with the same name as newName.
     */
    public static boolean checkSamePicture (Photo photo, String newName){
        Directory currentDirectory = getCurrentDirectory(photo);
        if(currentDirectory.checkSameName(newName)){
            return true;
        }
        else{
            return false;
        }
    }

  /**
   * Returns the directory that photo is under. Uses a helper to recurse through the tree.
   *
   * @param photo Photo object we need to find.
   * @return The Directory that this photo is under.
   */
  public static Directory getCurrentDirectory(Photo photo) {
    String pattern = Pattern.quote(System.getProperty("file.separator"));
    String path = photo.getPath().substring(0, photo.getPath().lastIndexOf(File.separator));
    String[] p = path.split(pattern);
    return helper(tree, p, startIndex).getDir();
  }

  /**
   * Returns the directory object based on the path given by dirpath.
   * Uses a helper to recurse through the tree.
   *
   * @param     dirPath     The path of the directory that we are looking for.
   * @return    The directory of dirPath.
   */
  public static Directory getCurrentDirectory(String dirPath) {
    String pattern = Pattern.quote(System.getProperty("file.separator"));
    String[] p = dirPath.split(pattern);
    return helper(tree, p, startIndex).getDir();
  }

  /**
   * Recurse through the tree to get to the designated directory specified by s[].
   *
   * @param     dir     The current directory.
   * @param     s       List of string representation ov individual directory name.
   * @param     i       Indicator of the index in s[].
   * @return    Returns the corresponding Node<Directory> with s[i]. Final return when the i is the last index of s[]. Returns null if this tree does not contain the corresponding s[i] directory.
   */
  private static Node<Directory> helper(Node<Directory> dir, String s[], int i) {
    if (i == s.length) {
      return dir;
    } else {
      ArrayList<Node<Directory>> children = dir.getChildren();
      for (Node<Directory> node : children) {
        if (node.getDir().getName().equalsIgnoreCase(s[i])) {
          if (i == s.length - 1) {
            return node;
          }
          helper(node, s, i + 1);
        }
      }
    }
    return null;
  }

  /**
   * Returns the top node, root of the tree.
   *
   * @return The root of the tree.
   */
  public static Node<Directory> getTree() {
    return tree;
  }
}
