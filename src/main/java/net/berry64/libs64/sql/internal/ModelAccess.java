/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql.internal;

import net.berry64.libs64.sql.DBData;
import net.berry64.libs64.sql.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAccess {
    private Lib64SQL sql;

    public ModelAccess(Lib64SQL sql) {
        this.sql = sql;
    }

    public <E extends Model> List<E> fetchAll(E incompleteModel){
        List<E> ret = new ArrayList<>();

        ModelData mdata = sql.registry.registry.get(incompleteModel.getClass());

        ResultSet rs = fetch(mdata, incompleteModel);

        if(rs == null)
            return ret; //returns an empty list

        try {
            while (rs.next()) {
                try {
                    ret.add((E) ModelParser.constructModel(incompleteModel.getClass(), mdata, rs));
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

    <E extends Model> boolean update(E m){
        return m.update();
    }

    public <E extends Model> E fetchOne(E incompleteModel){
        ModelData mdata = sql.getRegistry().get(incompleteModel.getClass());
        ResultSet rs = fetch(mdata, incompleteModel);
        if(rs == null)
            return null;

        try {
            if(rs.next())
                return (E) ModelParser.constructModel(incompleteModel.getClass(), mdata, rs);
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
            //TODO Unregistered Model
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

        return this.sql.modelSQL.executeQuery(sql.toString());
    }
}
