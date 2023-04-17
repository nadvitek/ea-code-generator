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
 * User
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-17T20:27:24.549543+02:00[Europe/Prague]")
public class User {

  @JsonProperty("cardNumber")
  private Long cardNumber;

  @JsonProperty("email")
  private String email;

  @JsonProperty("username")
  private String username;

  @JsonProperty("isStudent")
  private Boolean isStudent;

  public User cardNumber(Long cardNumber) {
    this.cardNumber = cardNumber;
    return this;
  }

  /**
   * Get cardNumber
   * @return cardNumber
  */
  @NotNull 
  @Schema(name = "cardNumber", required = true)
  public Long getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(Long cardNumber) {
    this.cardNumber = cardNumber;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  @NotNull 
  @Schema(name = "email", required = true)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
  */
  @NotNull 
  @Schema(name = "username", required = true)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public User isStudent(Boolean isStudent) {
    this.isStudent = isStudent;
    return this;
  }

  /**
   * Get isStudent
   * @return isStudent
  */
  @NotNull 
  @Schema(name = "isStudent", required = true)
  public Boolean getIsStudent() {
    return isStudent;
  }

  public void setIsStudent(Boolean isStudent) {
    this.isStudent = isStudent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.cardNumber, user.cardNumber) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.username, user.username) &&
        Objects.equals(this.isStudent, user.isStudent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cardNumber, email, username, isStudent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    cardNumber: ").append(toIndentedString(cardNumber)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    isStudent: ").append(toIndentedString(isStudent)).append("\n");
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

