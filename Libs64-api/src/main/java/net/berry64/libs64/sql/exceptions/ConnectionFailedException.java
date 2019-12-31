package net.berry64.libs64.sql.exceptions;

public class ConnectionFailedException extends RuntimeException {

    public ConnectionFailedException(String message) {
        super(message);
    }
}
