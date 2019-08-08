package net.berry64.libs64.sql;

import net.berry64.libs64.sql.database.MYSQL;
import net.berry64.libs64.sql.database.SQLITE;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Lib64SQL {
    protected Connection connection;

    protected abstract Connection createConnection() throws SQLException;

    public static Lib64SQL createMySQL(String url, int port, String database, String username, String password) {
        return new MYSQL(url, port, database, username, password);
    }

    public static Lib64SQL createSQLite(File file) {
        return new SQLITE(file);
    }

    private class ModelData{
        private PreparedStatement insertStatement = null;
        private List<Field> affectedColumns = null;
        private String tableName = null;

        ModelData() {}

        ModelData setInsertStatement(PreparedStatement insertStatement) {
            this.insertStatement = insertStatement;
            return this;
        }

        ModelData setAffectedColumns(List<Field> affectedColumns) {
            this.affectedColumns = affectedColumns;
            return this;
        }

        ModelData setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }
    }
    private static Map<Class<? extends org.bukkit.plugin.Plugin>, String> pluginName = new HashMap<>();
    private Map<Class<? extends Model>, ModelData> modelDatas = new HashMap<>();

    public boolean register(Plugin pl, Class<? extends Model> clazz) {
        checkConnection();
        if(modelDatas.containsKey(clazz))
            return false;

        //construct table name
        String tbname = pl.getName() + "_" + clazz.getSimpleName();
        if(tbname.length() > 64)
            throw new ConnectionFailedException("SQL table name exceeds length limit: 64");
        /* too complicated, lets just throw an error
        int i = 5;
        while (tbname.length() > 64 || hasTableName(tbname)) { //greater than most sql threshold, trying to rebuild name
            /*
            we wanna make sure that the table name is still identifiable,
            so we do an (almost)50/50 split on the different names:
            32 characters of plugin name, 31 of class name, plus an underscore
             * /
            String plName = pluginName.get(pl.getClass()); // see if we have a cache
            if (plName == null) { //nope we dont, construct a name s.t. plugins are recognizable
                plName = pl.getName();
                if (plName.length() > 32)
                    plName = WordUtils.abbreviate(plName, i, 32);
            }

            String className = clazz.getSimpleName();
            if (className.length() > 31)
                className = WordUtils.abbreviate(className, i, 31);


            tbname = plName + "_" + className; //should be exactly 64 characters long

            if (i < 31)
                i++;
            else
                i--;
        }*/
        ModelData mdata = new ModelData().setTableName(tbname);

        List<Field> columns = new ArrayList<>();
        for(Field f : clazz.getDeclaredFields()){
            if(f.isAnnotationPresent(DBData.class))
                columns.add(f);
        }
        mdata.setAffectedColumns(columns);

        return true;
    }

    private boolean hasTableName(String name){
        for(ModelData md : modelDatas.values())
            if(md.tableName != null && md.tableName.equals(name))
                return true;
        return false;
    }
    private void checkConnection() {
        try {
            if (connection == null || connection.isClosed())
                connection = createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new ConnectionFailedException("Unable to ");
    }
    private static class ConnectionFailedException extends RuntimeException {
        ConnectionFailedException(String message) {
            super(message);
        }
    }
}
