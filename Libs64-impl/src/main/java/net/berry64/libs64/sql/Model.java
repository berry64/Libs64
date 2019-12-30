/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import java.lang.reflect.Field;
import java.util.List;

public abstract class Model {
    public boolean addTo(Lib64SQL sql) {
        return sql.addRow(this);
    }

    public boolean update(Lib64SQL sql, List<Field> pivot) {
        return sql.update(this, pivot);
    }

    public boolean update(Lib64SQL sql, Field... pivot) {
        return sql.update(this, pivot);
    }

    public boolean update(Lib64SQL sql) {
        return sql.update(this);
    }

    public boolean update(Lib64SQL sql, String... fieldnames) {
        return sql.update(this, fieldnames);
    }
}