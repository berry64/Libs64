package net.berry64.libs64.sql.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.berry64.libs64.sql.Lib64SQL;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class MYSQL extends Lib64SQL {

    public MYSQL(String url, int port, String database, String username, String password) {
        super();
    }

    @Override
    protected Connection createConnection() {
        return null;
    }
}
