package net.berry64.libs64.sql.operations;

import net.berry64.libs64.sql.Lib64SQL;
import net.berry64.libs64.sql.Row;

import java.sql.SQLException;
import java.util.List;

public abstract class OperationBase {
    private Lib64SQL db;
    OperationBase(Lib64SQL db){
        this.db = db;
    }
    OperationBase(){}

    public boolean execute(){
        try {
            unsafe();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean unsafe() throws SQLException{
        return execute(db);
    }
    abstract boolean execute(Lib64SQL db) throws SQLException;

    public Row fetchone(){return null;}
    public Row[] fetchall(int length){return null;}
    public List<Row> fetchall(){return null;}
}
