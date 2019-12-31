package net.berry64.libs64.sql;

import java.lang.reflect.Field;
import java.util.List;

public abstract class Model {

    public boolean addTo(Lib64SQL sql) {
        return true;
    }

    public boolean update(Lib64SQL sql, List<Field> pivot) {
        return true;
    }

    public boolean update(Lib64SQL sql, Field... pivot) {
        return true;
    }

    public boolean update(Lib64SQL sql) {
        return true;
    }

    public boolean update(Lib64SQL sql, String... fieldnames) {
        return true;
    }
}
