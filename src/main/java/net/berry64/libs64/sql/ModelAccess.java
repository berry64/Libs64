/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import net.berry64.libs64.sql.exceptions.ConnectionFailedException;
import net.berry64.libs64.sql.exceptions.InvalidModelException;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

class ModelAccess {
    private Lib64SQL sql;

    ModelAccess(Lib64SQL sql) {
        this.sql = sql;
    }

    public <E extends Model> List<E> fetchAll(E incompleteModel) {
        List<E> ret = new ArrayList<>();

        ModelData mdata = sql.registry.getModelData(incompleteModel.getClass());

        ResultSet rs = fetch(mdata, incompleteModel, "");

        if (rs == null)
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public <E extends Model> E fetchOne(E incompleteModel) {
        ModelData mdata = sql.registry.getModelData(incompleteModel.getClass());
        ResultSet rs = fetch(mdata, incompleteModel, " limit 1");
        if (rs == null)
            return null;

        try {
            if (rs.next()) {
                E model = (E) ModelParser.constructModel(incompleteModel.getClass(), mdata, rs);
                rs.close();
                return model;
            } else {
                rs.close();
                return null;
            }
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param mdata
     * @param incompleteModel
     * @param <E>
     * @return
     * @deprecated Result set is not closed
     */
    public <E extends Model> ResultSet fetch(ModelData mdata, E incompleteModel, String extra) {
        if (mdata == null) {
            //TODO Unregistered Model
            return null;
        }

        Map<String, Object> known_values = new LinkedHashMap<>();
        mdata.affectedColumns.forEach(field -> {
            Object value = null;
            try {
                value = field.get(incompleteModel);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (value != null) {
                String name = field.getAnnotation(DBData.class).name();
                if (name.isEmpty())
                    name = field.getName();
                known_values.put(name, value);
            }
        });

        if (known_values.isEmpty()) {
            return null;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(mdata.tableName).append(" WHERE ");

        boolean first = true;
        for (Map.Entry<String, Object> e : known_values.entrySet()) {
            if (!first)
                sql.append(" AND ");
            else
                first = false;

            sql.append(e.getKey()).append("=").append("?");
        }
        PreparedStatement ps = this.sql.prepareStatement(sql.append(extra).toString());
        int index = 1;
        try {
            for (Map.Entry<String, Object> e : known_values.entrySet()) {
                ps.setObject(index++, e.getValue());
            }
            return ps.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ConnectionFailedException("Unable to execute query: "+ps.toString());
        }
    }

    public boolean update(Model model, List<Field> pivot) {
        if (pivot == null)
            throw new NullPointerException("Empty pivot defined in model " + model.getClass().getName());

        List<Object> pivotValues = new ArrayList<>();
        List<Object> updatevalues = new ArrayList<>();
        ModelData mdata = sql.registry.getModelData(model.getClass());
        StringBuilder set = new StringBuilder("UPDATE ").append(mdata.tableName).append(" SET ");
        StringBuilder where = new StringBuilder(" WHERE ");
        boolean setfirst = true, wherefirst = true;

        for (Field f : mdata.affectedColumns) {
            Object value = null;
            try {
                value = f.get(model);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
            DBData dbData = f.getAnnotation(DBData.class);
            if (pivot.contains(f)) {
                if (wherefirst)
                    wherefirst = false;
                else
                    where.append(" AND ");
                if (value != null) {
                    pivotValues.add(value);
                    where.append(ModelRegistry.getDBDataName(f)).append("=?");
                } else
                    where.append(ModelRegistry.getDBDataName(f)).append(" IS NULL");
            } else {
                if (setfirst)
                    setfirst = false;
                else
                    set.append(", ");
                if (value != null) {
                    updatevalues.add(value);
                    set.append(ModelRegistry.getDBDataName(f)).append("=?");
                } else
                    set.append(ModelRegistry.getDBDataName(f)).append("=NULL");
            }
        }

        PreparedStatement ps = sql.prepareStatement(set.append(where).toString());
        int index = 1;
        try {
            for (Object e : updatevalues)
                ps.setObject(index++, e);
            for (Object e : pivotValues)
                ps.setObject(index++, e);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Model model, Field... pivot) {
        return update(model, Arrays.asList(pivot));
    }

    public boolean update(Model model) {
        //uses the primary key as pivot
        ModelData data = sql.registry.getModelData(model.getClass());
        Field pivot = null;
        for (Field f : data.affectedColumns) {
            DBData dbData = f.getAnnotation(DBData.class);
            if (dbData.PrimaryKey())
                pivot = f;
        }
        if (pivot == null)
            throw new InvalidModelException("Unable to find Primary Key when updating model: " + model.getClass().getName());
        return update(model, pivot);
    }

    public boolean update(Model model, String... fieldnames) {
        Class<? extends Model> clazz = model.getClass();
        List<Field> fields = new ArrayList<>();
        for (String name : fieldnames) {
            Field f = getField(clazz, name);
            if (f == null)
                return false;
            else
                fields.add(f);
        }
        return update(model, fields);
    }


    public static Field getField(Class<?> c, String name) {
        try {
            Field f = c.getDeclaredField(name);
            if (!f.isAccessible())
                f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
