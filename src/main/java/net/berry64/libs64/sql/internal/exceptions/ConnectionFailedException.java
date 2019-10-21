package net.berry64.libs64.sql.internal.exceptions;

public class ConnectionFailedException extends RuntimeException{
    public ConnectionFailedException(String message) {
        super(message);
    }
}
