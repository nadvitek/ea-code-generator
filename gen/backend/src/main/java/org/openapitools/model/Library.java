package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.model.Book;
import org.openapitools.model.User;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Library
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-17T20:27:24.549543+02:00[Europe/Prague]")
public class Library {

  @JsonProperty("adress")
  private String adress;

  @JsonProperty("User")
  @Valid
  private List<User> user = new ArrayList<>();

  @JsonProperty("name")
  private String name;

  @JsonProperty("Book")
  @Valid
  private List<Book> book = new ArrayList<>();

  public Library adress(String adress) {
    this.adress = adress;
    return this;
  }

  /**
   * Get adress
   * @return adress
  */
  @NotNull 
  @Schema(name = "adress", required = true)
  public String getAdress() {
    return adress;
  }

  public void setAdress(String adress) {
    this.adress = adress;
  }

  public Library user(List<User> user) {
    this.user = user;
    return this;
  }

  public Library addUserItem(User userItem) {
    this.user.add(userItem);
    return this;
  }

  /**
   * Get user
   * @return user
  */
  @NotNull @Valid 
  @Schema(name = "User", required = true)
  public List<User> getUser() {
    return user;
  }

  public void setUser(List<User> user) {
    this.user = user;
  }

  public Library name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull 
  @Schema(name = "name", required = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Library book(List<Book> book) {
    this.book = book;
    return this;
  }

  public Library addBookItem(Book bookItem) {
    this.book.add(bookItem);
    return this;
  }

  /**
   * Get book
   * @return book
  */
  @NotNull @Valid 
  @Schema(name = "Book", required = true)
  public List<Book> getBook() {
    return book;
  }

  public void setBook(List<Book> book) {
    this.book = book;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Library library = (Library) o;
    return Objects.equals(this.adress, library.adress) &&
        Objects.equals(this.user, library.user) &&
        Objects.equals(this.name, library.name) &&
        Objects.equals(this.book, library.book);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adress, user, name, book);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Library {\n");
    sb.append("    adress: ").append(toIndentedString(adress)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    book: ").append(toIndentedString(book)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

