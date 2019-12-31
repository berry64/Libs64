package net.berry64.libs64.sql.database;

import net.berry64.libs64.sql.Lib64SQL;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLITE extends Lib64SQL {

    public SQLITE(File file) {
    }

    @Override
    protected Connection createConnection() throws SQLException {
        return null;
    }
}
