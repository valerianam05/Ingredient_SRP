package org.spring.ingredient_srp.exception;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
  public DatabaseException(String message, SQLException e) {
    super(message);
  }
}
