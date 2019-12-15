/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import net.berry64.libs64.sql.exceptions.ConnectionFailedException;

import java.sql.*;

class SQLOperation {
    public Connection connection = null;
    private Lib64SQL sql;

    public SQLOperation(Lib64SQL sql) {
        this.sql = sql;
    }

    public boolean checkConnection(){
        try {
            if(connection == null || connection.isClosed())
                connection = sql.createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionFailedException("Unable to connect to Database");
        }
        return true;
    }

    /**
     * @deprecated
     * @param sql
     * @return
     */
    public boolean execute(String sql){
        try {
            Statement s = connection.createStatement();
            boolean rs = s.execute(sql);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @deprecated
     * @param sql
     * @return
     */
    public int executeUpdate(String sql){
        try {
            Statement s = connection.createStatement();
            int rs = s.executeUpdate(sql);
            s.close();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * Executes raw sql Command on the database
     * @deprecated as ResultSet is not closed and neither is the connection
     * @param sql the SQL command
     * @return the Result
     */
    public ResultSet executeQuery(String sql){
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);
            s.close();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement prepareStatement(String sql){
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
