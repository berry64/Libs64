package net.berry64.libs64.sql.operations;

import net.berry64.libs64.sql.Column;
import net.berry64.libs64.sql.Lib64SQL;
import net.berry64.libs64.sql.Table;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class InsertOperation extends OperationBase {
    private Map<Column, Object> values;

    public InsertOperation(Lib64SQL db, Map<Column, Object> values) {
        super(db);
        this.values = values;

        Map<Table, Set<Column>> affectedTables = new LinkedHashMap<>();
        for(Column c : values.keySet()){
            Set<Column> cur = affectedTables.get(c.getTable());
            if(cur != null)
                cur.add(c);
            else {
                cur = new LinkedHashSet<>();
                cur.add(c);
                affectedTables.put(c.getTable(), cur);
            }
        }

        int tableCount = affectedTables.keySet().size();
        sql = new String[tableCount];
        int table = 0;
        for(Table t : affectedTables.keySet()){
            StringBuilder sql = new StringBuilder("INSERT INTO ");
            sql.append(t.getName()).append('(');

            boolean first = true;
            StringBuilder value = new StringBuilder();
            for(Column c : affectedTables.get(t)){
                if(!first)
                    sql.append(',');
                else
                    first = false;
                sql.append(c.getName());

                value.append(values);
            }
            sql.append(") VALUES(");
            table++;
        }
    }

    String sql[];

    @Override
    public boolean execute(Lib64SQL db) throws SQLException {
        Statement s = db.getConnection().createStatement();
        return false;
    }
}

class PreparedStatementCache {
    
}