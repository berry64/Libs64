package net.berry64.libs64.sql;

import net.berry64.libs64.sql.database.*;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * @author berry64
 */
public abstract class Lib64SQL {
    protected Connection connection;

    protected abstract Connection createConnection();

    public static Lib64SQL createMySQL(String url, int port,String database, String username, String password){
        return new MYSQL(url, port,database, username, password);
    }
    public static Lib64SQL createSQLite(File file){
        return new SQLITE(file);
    }


    private Map<String, Table> tables = new HashMap<>();

    public Table createTable(String name, Column... columns){
        checkConnection();

        if(tables.containsKey(name))
            return tables.get(name).matchColumns(columns);

        try {
            Statement s = connection.createStatement();
            StringBuilder sql = new StringBuilder("CREATE TABLE ");
            sql.append(name).append(" (");
            for(int i = 0; i < columns.length; i++){
                if (i > 0)
                    sql.append(", ");
                sql.append(columns[i].getName()).append(" ");
                sql.append(columns[i].getTypeString());
            }
            s.execute(sql.append(")").toString());
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        Table tb = new Table(this, name, Arrays.asList(columns));
        tables.put(name, tb);
        return tb;
    }

    public void deleteTable(Table table){
        deleteTable(table.getName());
    }
    public void deleteTable(String name){
        try {
            Statement s = connection.createStatement();
            s.execute("DROP TABLE "+name);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clearTable(Table table){
        clearTable(table.getName());
    }
    public void clearTable(String name){
        try {
            Statement s = connection.createStatement();
            s.execute("TRUNCATE TABLE "+name);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Table getTable(String name){
        return tables.get(name);
    }

    public boolean refresh(){
        tables.clear();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        connection = createConnection();
        loadFromExisting();
        return true;
    }

    public void loadFromExisting(){
        checkConnection();
        List<String> tables = new ArrayList<>();

        try {
            //Obtain tables
            DatabaseMetaData dbmeta = connection.getMetaData();
            ResultSet rs = dbmeta.getTables(null, null, null, new String[]{"TABLES"});
            while(rs.next())
                tables.add(rs.getString("TABLE_NAME"));
            rs.close();

            //Obtain table details
            for(String tablename : tables){
                Statement s = connection.createStatement();
                rs = s.executeQuery("SELECT * FROM "+tablename);
                ResultSetMetaData rsMeta = rs.getMetaData();
                int columnCount = rsMeta.getColumnCount();

                List<Column> columns = new ArrayList<>();
                for(int i = 1; i <=columnCount; i++){
                    String name = rsMeta.getColumnName(i);
                    DataType type = DataType.fromType(rsMeta.getColumnType(i), name);
                    columns.add(new Column(name, type));
                }
                this.tables.put(tablename, new Table(this, tablename, columns));
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean executeStatement(String statement){
        try {
            Statement s = getConnection().createStatement();
            s.execute(statement);
            s.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Connection getConnection(){
        if(connection == null)
            connection = createConnection();
        return connection;
    }

    private void checkConnection() throws NullPointerException{
        try {
            if(connection == null)
                createConnection();
            if(connection != null && !connection.isClosed() && connection.isValid(10))
                return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Connection is null or closed/invalid");
    }
}
