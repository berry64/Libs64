package net.berry64.libs64.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The cached representation of a SQL Table
 *
 * @author berry64
 */
public class Table implements Cloneable{
    private final Lib64SQL db;
    private final String name;
    private final Map<String, Column> columns = new HashMap<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @deprecated use from {@link Lib64SQL#createTable(String, Column...)}
     *
     * Creates a Table, usually used from {@link Lib64SQL#createTable(String, Column...)}
     * @param database the database
     * @param name name of the table
     * @param columns the columns to start with
     */
    public Table(Lib64SQL database, String name, List<Column> columns) {
        db = database;
        this.name = name;
        columns.forEach(c -> {
            this.columns.put(c.getName(), c);
        });
    }

    /**
     * Gets the {@link Lib64SQL} database the table is in
     *
     * @return the "parent" database
     */
    public Lib64SQL getDatabase(){
        return db;
    }

    /**
     * Finds and creates the columns not in cache but in the list provided using {@link #addColumn(String, DataType)}
     *
     * @param columns The columns to compare with
     * @return The current table
     */
    public Table matchColumns(Column... columns){
        for(Column c : columns){
            if(!this.columns.keySet().contains(c.getName()))
                addColumn(c);
        }
        return this;
    }

    /**
     * Attempts to obtain a cached {@link Column} by the column name
     * @param name the column name
     * @return the column, null if name not found
     */
    public Column getColumn(String name){
        return columns.get(name);
    }

    /**
     * Gets the name of the table
     * @return table name
     */
    public String getName(){
        return name;
    }

    /**
     * Gets all the cached columns in form of an array
     * @return the array of columns
     */
    public Column[] getColumns(){
        return columns.values().toArray(new Column[0]);
    }

    /**
     * Adds a column to the current table
     *
     * Libs64 completes this by executing "ALTER TABLE table_name ADD column_name column_type" using {@link Lib64SQL#executeStatement(String)}
     * @param column The column to be added
     * @return The current table
     */
    public Table addColumn(Column column){
        if(columns.containsKey(column.getName()))
            return this;

        if(column.getType() == DataType.UUID && !DataType.isUIDName(column.getName()))
            column.setName(DataType.toUIDName(name));
        String sql = "ALTER TABLE " + name + " ADD " + column.getName() +
                " " + column.getTypeString();
        if(db.executeStatement(sql))
            columns.put(column.getName(), column);
        return this;
    }

    /**
     * @see #hasColumn(String)
     */
    public boolean hasColumn(Column column){
        return hasColumn(column.getName());
    }

    /**
     * Checks to see whether a column with a specific name exists or not
     *
     * does NOT check the type of the column
     * @param name The name to be checked
     * @return <tt>true</tt> if column with that name, case-sensitive, is found, <tt>false</tt> otherwise
     */
    public boolean hasColumn(String name){
        return columns.containsKey(name);
    }


    /**
     * @see #addColumn(String, DataType)
     */
    public Table addColumn(String name, DataType type){
        return  addColumn(new Column(name, type));
    }


    /**
     * @see #deleteColumn(String)
     */
    public Table deleteColumn(Column column){
        return deleteColumn(column.getName());
    }

    /**
     * Deletes a specific column
     *
     * Libs64 completes this by executing "ALTER TABLE table_name DROP COLUMN" from db using {@link Lib64SQL#executeStatement(String)}
     * @param name The name of the column
     * @return The current table
     */
    public Table deleteColumn(String name){
        String sql = "ALTER TABLE "+this.name+" DROP COLUMN "+name;
        if(db.executeStatement(sql))
            columns.remove(name);
        return this;
    }
}
