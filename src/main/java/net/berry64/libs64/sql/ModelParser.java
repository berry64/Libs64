/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class ModelParser {
    private static ObjenesisStd objenesis = new ObjenesisStd();
    private static Map<Class<? extends Model>, ObjectInstantiator<?>> instantiators = new HashMap<>();

    public static Object constructModel(Class<? extends Model> clazz, ModelData mdata, ResultSet input) throws IllegalAccessException, InstantiationException, SQLException {
        ObjectInstantiator<?> inst = instantiators.get(clazz);
        if(inst == null)
            instantiators.put(clazz, (inst=objenesis.getInstantiatorOf(clazz)));

        Object o = inst.newInstance();
        for (Field f : mdata.affectedColumns) {
            DBData fieldData = f.getAnnotation(DBData.class);
            String name;
            if (fieldData == null || fieldData.name().isEmpty()) {
                name = f.getName();
            } else {
                name = fieldData.name();
            }
            f.set(o, input.getObject(name));
        }
        return o;
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
}
