package net.berry64.libs64.sql.operations;

import net.berry64.libs64.sql.Column;
import net.berry64.libs64.sql.Lib64SQL;
import net.berry64.libs64.sql.Row;
import net.berry64.libs64.sql.Table;
import net.berry64.libs64.sql.operations.conditions.ConditionBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SelectOperation extends OperationBase {
    //SELECT column FROM table WHERE

    private ConditionBase condition;
    private Set<Column> columns;

    public SelectOperation(Lib64SQL db, Set<Column> columns, ConditionBase condition){
        super(db);
        this.columns = columns;
        this.condition = condition;

        //build PreparedStatements
        StringBuilder sql = new StringBuilder("SELECT ");

        boolean first = true;

        Set<Table> affectedTables = new HashSet<>();
        columns.forEach(c -> affectedTables.add(c.getTable()));

        for(Column c : columns){
            if(!first)
                sql.append(',');
            else
                first = false;

            sql.append(c.getTable().getName())
                    .append('.')
                    .append(c.getName());
        }
        sql.append(" FROM ");

        first = true;
        for(Table t : affectedTables){
            if(!first)
                sql.append(',');
            else
                first = false;

            sql.append(t.getName());
        }

        if(condition != null)
            sql.append(" WHERE ").append(condition.getSQLString());

        String str = sql.toString();
        try {
            fetchOne = db.getConnection().prepareStatement(str+" LIMIT 1");
            fetchMulti = db.getConnection().prepareStatement(str+" LIMIT ?");
            fetchAll = db.getConnection().prepareStatement(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(Lib64SQL db) throws SQLException {
        //does nothing but returns whether or not a result exists
        return fetchone() != null;
    }

    private PreparedStatement fetchOne, fetchMulti, fetchAll;

    //ok so this is where all the memory gets used up

    @Override
    public Row fetchone() {
        Row result = null;

        try {
            ResultSet rs = fetchOne.executeQuery();
            if(rs.next())
                result = buildRow(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Row[] fetchall(int length) {
        Row[] results = new Row[length];

        try {
            fetchMulti.setInt(1, length);
            ResultSet rs = fetchMulti.executeQuery();

            for (int i = 0; i < length; i++) {
                if(!rs.next())
                    break;
                results[i] = buildRow(rs);
            }
            rs.close();
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Row> fetchall() {
        List<Row> result = new ArrayList<>();
        try {
            ResultSet rs = fetchAll.executeQuery();

            while(rs.next())
                result.add(buildRow(rs));

            rs.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Row buildRow(ResultSet rs){
        Row row = new Row();
        try {
            for (Column c : columns) {
                switch (c.getType()) {
                    case INT:
                        row.appendData(c, rs.getInt(c.getName())); break;
                    case UUID:
                        row.appendData(c, UUID.fromString(rs.getString(c.getName()))); break;
                    case BOOLEAN:
                        row.appendData(c, rs.getBoolean(c.getName())); break;
                    case DOUBLE:
                        row.appendData(c, rs.getDouble(c.getName())); break;
                    case TEXT:
                        row.appendData(c, rs.getString(c.getName())); break;
                    case DATE:
                        row.appendData(c, rs.getDate(c.getName())); break;
                    default:
                        row.appendData(c, rs.getObject(c.getName())); break;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return row;
    }
}
