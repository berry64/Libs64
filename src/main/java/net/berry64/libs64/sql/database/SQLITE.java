package net.berry64.libs64.sql.database;

import net.berry64.libs64.sql.Lib64SQL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLITE extends Lib64SQL {
    private static final String driverClass = "org.sqlite.JDBC";


    private final File file;

    public SQLITE(File file) {
        this.file = file;
    }

    private String constructURL(){
        return "jdbc:sqlite:"+file.getAbsolutePath();
    }

    @Override
    protected Connection createConnection() throws SQLException{
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(constructURL());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
