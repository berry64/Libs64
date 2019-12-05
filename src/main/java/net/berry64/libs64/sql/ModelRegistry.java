/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import net.berry64.libs64.ConnectionFailedException;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ModelRegistry {
    public Map<Class<? extends Model>, ModelData> registry = new HashMap<>();
    Lib64SQL sql;

    public ModelRegistry(Lib64SQL sql) {
        this.sql = sql;
    }

    public boolean register(Plugin pl, Class<? extends Model> clazz) {

        if(registry.containsKey(clazz))
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
        StringBuilder values = new StringBuilder(" VALUES(");
        boolean first = true;
        for (Field column : columns) {
            if(first)
                first = false;
            else {
                sql.append(", ");
                values.append(", ");
            }

            String name = column.getAnnotation(DBData.class).name();
            if(name.isEmpty())
                name = column.getName();

            sql.append(name);
            values.append("?");
        }
        sql.append(')').append(values.append(')').toString());

        mdata.insertStatement = this.sql.prepareStatement(sql.toString());

        registry.put(clazz, mdata);

        // TODO create table if not exist with specified arguments
        return true;
    }
}
