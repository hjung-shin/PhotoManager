package model;

import java.io.Serializable;

/**
 * This is a Tag class. It is tagged to a Photo object.
 */
public class Tag implements Serializable {
  /** Name of this tag */
  private String name;

  /**
   * the constructor of this Tag class
   *
   * @param name the name of this Tag (or content).
   */
  public Tag(String name) {
    this.name = "@" + name;
  }

  /**
   * the getter of this Tag class
   *
   * @return this.name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Equal function of this Tag object. They are equal if they have the same name.
   */
  public boolean equals(Object other) {
    if (other instanceof Tag) {
      Tag tag = (Tag) other;
      return this.name.equals(tag.getName());
    }
    return false;
  }
}
