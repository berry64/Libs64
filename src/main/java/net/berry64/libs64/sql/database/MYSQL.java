package net.berry64.libs64.sql.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.berry64.libs64.sql.Lib64SQL;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class MYSQL extends Lib64SQL {
    private final String url;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private static final String[] args = new String[]{
        "autoReconnect=1",
        "useUnicode=true",
        "characterEncoding=utf-8"
    };
    private static final String driverClass = "com.mysql.jdbc.Driver";

    public MYSQL(String url, int port, String database, String username, String password) {
        super();
        this.url = url;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    private String constructURL(){
        StringBuilder sb = new StringBuilder("jdbc:mysql://");
        sb.append(url).append(":").append(port).append("/").append(database);
        for(int i = 0; i < args.length; i++){
            if(i > 0)
                sb.append('&');
            else
                sb.append('?');
            sb.append(args[i]);
        }
        return sb.toString();
    }


    @Override
    protected Connection createConnection() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(driverClass);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            return null;
        }
        cpds.setJdbcUrl(constructURL());
        cpds.setUser(username);
        cpds.setPassword(password);
        cpds.setMaxStatements(180);
        try {
            return cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
