package net.berry64.libs64.sql;

import net.berry64.libs64.sql.database.MYSQL;
import net.berry64.libs64.sql.database.SQLITE;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class Lib64SQL {
    protected Connection connection = null;

    protected abstract Connection createConnection() throws SQLException;

    public static Lib64SQL createMySQL(String url, int port,String database, String username, String password){
        return new MYSQL(url, port,database, username, password);
    }
    public static Lib64SQL createSQLite(File file){
        return new SQLITE(file);
    }

    private class ModelData{
        private Class<? extends Model> clazz = null;
        private PreparedStatement insertStatement = null;
        private List<Field> affectedColumns = null;
        private String tableName = null;
        private Plugin plugin = null;

        ModelData() {}
    }
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
        ModelData mdata = new ModelData();
        mdata.clazz = clazz;
        mdata.tableName = tbname;
        mdata.plugin = pl;

        List<Field> columns = new ArrayList<>();
        for(Field f : clazz.getDeclaredFields()){
            if(f.isAnnotationPresent(DBData.class)) {
                if(!f.isAccessible())
                    f.setAccessible(true);
                columns.add(f);
            }
        }

        //generate insert statement
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tbname).append('(');
        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i).getName());
        }
        sql.append(')');


        modelDatas.put(clazz, mdata);
        return true;
    }

    public <E extends Model> List<E> fetchAll(E incompleteModel){
        List<E> ret = new ArrayList<>();

        ModelData mdata = modelDatas.get(incompleteModel.getClass());

        ResultSet rs = fetch(mdata, incompleteModel);

        if(rs == null)
            return ret; //returns an empty list

        try {
            while (rs.next()) {
                try {
                    ret.add((E) constructModel(incompleteModel.getClass(), mdata, rs));
                } catch (IllegalAccessException | InstantiationException e) {
                    //TODO Log unable to construct Model
                    e.printStackTrace();
                }
            }
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ret;
    }
    public <E extends Model> E fetchOne(E incompleteModel){
        ModelData mdata = modelDatas.get(incompleteModel.getClass());
        ResultSet rs = fetch(mdata, incompleteModel);
        if(rs == null)
            return null;

        try {
            if(rs.next())
                return (E) constructModel(incompleteModel.getClass(), mdata, rs);
            else
                return null;
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @deprecated Result set is not closed
     * @param mdata
     * @param incompleteModel
     * @param <E>
     * @return
     */
    public <E extends Model> ResultSet fetch(ModelData mdata, E incompleteModel){
        if(mdata == null){
            //TODO Log error Unregistered Model
            return null;
        }

        Map<String, Object> known_values = new HashMap<>();
        mdata.affectedColumns.forEach(field -> {
            Object value = null;
            try {
                value = field.get(incompleteModel);
            }catch (IllegalAccessException e) {
                e.printStackTrace();}
            if(value != null){
                String name = field.getAnnotation(DBData.class).name();
                if(name.isEmpty())
                    name = field.getName();
                known_values.put(name, value);
            }
        });

        if(known_values.isEmpty()){
            return null;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(mdata.tableName).append(" WHERE ");

        boolean first = true;
        for (Map.Entry<String, Object> e: known_values.entrySet()) {
            if(!first)
                sql.append(" AND ");
            else
                first = false;

            sql.append(e.getKey()).append("=").append(e.getValue().toString());
        }

        return executeQuery(sql.toString());
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
    private boolean hasTableName(String name){
        for(ModelData md : modelDatas.values())
            if(md.tableName != null && md.tableName.equals(name))
                return true;
        return false;
    }
    private void checkConnection() {

    private void checkConnection(){
        try {
            if(connection == null || connection.isClosed())
                connection = createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionFailedException("Unable to connect to Database");
        }

    }

    private static Object constructModel(Class<? extends Model> clazz, ModelData mdata, ResultSet input) throws IllegalAccessException, InstantiationException, SQLException {
        Object o = clazz.newInstance();
        for(Field f : mdata.affectedColumns){
            DBData fieldData = f.getAnnotation(DBData.class);
            String name;
            if(fieldData == null || fieldData.name().isEmpty()){
                name = f.getName();
            } else {
                name = fieldData.name();
            }

            /*Class<?> type = f.getType();
            if(type == int.class || type == Integer.class) {
                f.setInt(o, input.getInt(name));
            } else if(type == byte.class || type == Byte.class) {
                f.setByte(o, input.getByte(name));
            } else if (java.io.InputStream.class.isAssignableFrom(type)){
                f.set(o, input);
            } else if (type.isArray()){
                f.set(o, input.getArray(name));
            }  else if (type == double.class){
            } else {
                f.set(o, input.getObject(name));
            } DEAD CODE*/
            f.set(o, input.getObject(name));
        }
        return o;
    }
    private static class ConnectionFailedException extends RuntimeException{
        ConnectionFailedException(String message) {
            super(message);
        }
    }
}
