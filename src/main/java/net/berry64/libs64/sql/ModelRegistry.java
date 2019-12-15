/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import net.berry64.libs64.sql.database.SQLITE;
import net.berry64.libs64.sql.exceptions.ConnectionFailedException;
import net.berry64.libs64.sql.exceptions.InvalidModelException;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static net.berry64.libs64.sql.DataType.setAnnotationDataTypeField;

class ModelRegistry {
    Map<Class<? extends Model>, ModelData> registry = new HashMap<>();
    Lib64SQL sql;

    public ModelRegistry(Lib64SQL sql) {
        this.sql = sql;
    }

    public boolean register(Plugin pl, Class<? extends Model> clazz) {
        sql.checkConnection();
        if (registry.containsKey(clazz))
            return false;

        //construct table name
        String tbname = pl.getName() + "_" + clazz.getSimpleName();
        if (tbname.length() > 64)
            throw new ConnectionFailedException("SQL table name exceeds length limit: 64");

        ModelData mdata = new ModelData();
        mdata.clazz = clazz;
        mdata.tableName = tbname;
        mdata.plugin = pl;

        List<Field> columns = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(DBData.class)) {
                if (!f.isAccessible())
                    f.setAccessible(true);
                columns.add(f);
            }
        }
        mdata.affectedColumns = columns;
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

        //create table
        StringBuilder str = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tbname).append("(");
        StringBuilder insert = new StringBuilder("INSERT INTO ").append(tbname).append('(');
        StringBuilder values = new StringBuilder(" VALUES(");
        StringBuilder delete = new StringBuilder("DELETE FROM ").append(tbname).append(" WHERE ");
        String deletePivotName = null;
        boolean first = true;
        for (Field col : columns) {
            if (first)
                first = false;
            else {
                str.append(", ");
                insert.append(", ");
                values.append(", ");
            }
            if (col.getType().isPrimitive())
                throw new InvalidModelException("Field " + col.getName() + " in model " + clazz.getName() + " is primitive, this is not allowed");
            DBData data = col.getAnnotation(DBData.class);
            if (data.type() == DataType.AUTO)
                setAnnotationDataTypeField(col, data, DataType.getAutoDataType(col));
            str.append(getDBDataName(col)).append(" ");
            str.append(data.type().getType());
            if (data.PrimaryKey()) {
                str.append(" ").append("primary key ").append((sql instanceof SQLITE) ? "AUTOINCREMENT" : "AUTO_INCREMENT");

                //find delete primary key
                deletePivotName = getDBDataName(col);
            } else {
                //create insert statement
                insert.append(getDBDataName(col));
                values.append("?");
            }
            for (String tag : data.Tags())
                str.append(" ").append(tag);
            if (data.NotNull())
                str.append(" ").append("NOT NULL");

        }
        if (deletePivotName == null)
            throw new InvalidModelException("Model " + clazz.getName() + " does not have a Primary Key that is of type DataType.INT");

        str.append(")");
        insert.append(')').append(values.append(')').toString());
        try {
            Statement createTable = sql.getRawConnection().createStatement();
            createTable.executeUpdate(str.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionFailedException("Unable to create table: " + tbname);
        }
        mdata.insertStatement = sql.prepareStatement(insert.toString());
        mdata.deleteStatement = sql.prepareStatement(delete.append(deletePivotName).append("=?").toString());

        registry.put(clazz, mdata);
        return true;
    }

    public ModelData getModelData(Class<? extends Model> clazz) {
        return registry.get(clazz);
    }

    public boolean isRegistered(Class<? extends Model> clazz) {
        return registry.containsKey(clazz);
    }

    public boolean addRow(Model model) {
        if (!sql.checkConnection())
            return false;

        if (!isRegistered(model.getClass()))
            return false;

        ModelData mdata = getModelData(model.getClass());
        PreparedStatement ps = mdata.insertStatement;
        try {
            int index = 1;
            for (Field f : mdata.affectedColumns) {
                if (f.getAnnotation(DBData.class).PrimaryKey())
                    continue;
                ps.setObject(index++, f.get(model));
            }
            ps.executeUpdate();
            ps.clearParameters();
            return true;
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Model model) {
        Object pivotVal = null;
        ModelData mdata = getModelData(model.getClass());
        for (Field f : mdata.affectedColumns)
            if (f.getAnnotation(DBData.class).PrimaryKey())
                try {
                    pivotVal = f.get(model);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

        if (pivotVal == null)
            throw new NullPointerException("Cannot delete a model with type" + model.getClass().getName() + " that has a primary key value of null");

        try {
            mdata.deleteStatement.setObject(1, pivotVal);
            boolean ret =  mdata.deleteStatement.executeUpdate() > 0;
            mdata.deleteStatement.clearParameters();
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static String getDBDataName(Field field) {
        DBData data = field.getAnnotation(DBData.class);
        if (data == null || data.name().isEmpty())
            return field.getName();
        else
            return data.name();
    }
}
