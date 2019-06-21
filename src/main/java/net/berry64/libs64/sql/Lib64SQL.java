package net.berry64.libs64.sql;

import net.berry64.libs64.sql.database.MYSQL;
import net.berry64.libs64.sql.database.SQLITE;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class Lib64SQL {
    protected Connection connection;

    protected abstract Connection createConnection() throws SQLException;

    public static Lib64SQL createMySQL(String url, int port,String database, String username, String password){
        return new MYSQL(url, port,database, username, password);
    }
    public static Lib64SQL createSQLite(File file){
        return new SQLITE(file);
    }


    private void checkConnection(){
        try {
            if(connection == null || connection.isClosed())
                connection = createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new ConnectionFailedException("Unable to ");
    }
    private static class ConnectionFailedException extends RuntimeException{
        ConnectionFailedException(String message) {
            super(message);
        }
    }
}
