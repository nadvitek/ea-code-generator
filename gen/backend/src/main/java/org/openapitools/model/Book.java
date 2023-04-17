package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Book
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-17T20:27:24.549543+02:00[Europe/Prague]")
public class Book {

  @JsonProperty("name")
  private String name;

  @JsonProperty("yearOfPublishing")
  private Long yearOfPublishing;

  public Book name(String name) {
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

  public Book yearOfPublishing(Long yearOfPublishing) {
    this.yearOfPublishing = yearOfPublishing;
    return this;
  }

  /**
   * Get yearOfPublishing
   * @return yearOfPublishing
  */
  @NotNull 
  @Schema(name = "yearOfPublishing", required = true)
  public Long getYearOfPublishing() {
    return yearOfPublishing;
  }

  public void setYearOfPublishing(Long yearOfPublishing) {
    this.yearOfPublishing = yearOfPublishing;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return Objects.equals(this.name, book.name) &&
        Objects.equals(this.yearOfPublishing, book.yearOfPublishing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, yearOfPublishing);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Book {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    yearOfPublishing: ").append(toIndentedString(yearOfPublishing)).append("\n");
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

