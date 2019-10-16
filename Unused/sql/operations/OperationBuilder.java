package net.berry64.libs64.sql.operations;

import net.berry64.libs64.sql.Column;
import net.berry64.libs64.sql.Lib64SQL;
import net.berry64.libs64.sql.operations.conditions.ConditionBase;

import java.sql.SQLException;
import java.util.*;

public class OperationBuilder {
    private int type = -1;
    //private List<Column> targetColumns = new ArrayList<>();
    private ConditionBase condition;
    private Map<Column, Object> values = new TreeMap<>();

    public OperationBuilder(){}

    public OperationBuilder setType(OperationType type){
        this.type = type.getId();
        return this;
    }

    public OperationBuilder addColumn(Column... columns){
        //targetColumns.addAll(Arrays.asList(columns));
        for (Column column : columns)
            values.put(column, null);
        return this;
    }

    public OperationBuilder removeColumn(Column column){
        //targetColumns.remove(column);
        values.remove(column);
        return this;
    }

    public OperationBuilder clearColumns(){
        //targetColumns.clear();
        values.clear();
        return this;
    }

    public OperationBuilder setCondition(ConditionBase condition){
        this.condition = condition;
        return this;
    }

    public OperationBuilder setCondition(int type){
        this.type = type;
        return this;
    }

    public OperationBuilder setValue(Column column, Object value){
        values.put(column, value);
        return this;
    }

    public OperationBuilder clearValues(){
        values.clear();
        return this;
    }

    public OperationBuilder deleteColumnValue(Column column){
        values.put(column, null);
        return this;
    }

    public ConditionBase getCondition(){
        return condition;
    }

    public OperationBase build(){
        //fetch db, assumes everything is in same database as first connection, does not check
        if(values.isEmpty() || !values.keySet().iterator().hasNext())
            return new DummyOperation();
        Lib64SQL db = values.keySet().iterator().next().getTable().getDatabase();

        switch (type){
            case 0: return new SelectOperation(db, values.keySet(), condition);
            case 1:;
            case 2:;
            case 3:;
            default: throw new UnknownOperationTypeException("Unknown TypeID:" + type);
        }
    }
}
class DummyOperation extends OperationBase{
    @Override
    boolean execute(Lib64SQL db) throws SQLException {
        System.out.println("UNKNOWN OPERATION");
        return false;
    }
}
class UnknownOperationTypeException extends RuntimeException{
    UnknownOperationTypeException(String s) {
        super(s);
    }
}