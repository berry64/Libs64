/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql.exceptions;

public class ConnectionFailedException extends RuntimeException{
    public ConnectionFailedException(String message) {
        super(message);
    }
}
