package de.kaufland.ksilence.exception;

public class EntityExistsException extends RuntimeException{
    public EntityExistsException() {
        super("Entity already exists");
    }
}
