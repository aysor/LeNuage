package ru.netology.cloudservice.exception;

public class CloudServiceException extends RuntimeException {
  public CloudServiceException(String message) {
    super(message);
  }
}
