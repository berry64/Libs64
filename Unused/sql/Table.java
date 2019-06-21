package net.berry64.libs64.sql;

import net.berry64.libs64.sql.operations.OperationBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The cached representation of a SQL Table
 *
 * @author berry64
 */
public class Table implements Cloneable {
    private final Lib64SQL db;
    private final String name;
    private final Map<String, Column> columns = new HashMap<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @param database the database
     * @param name     name of the table
     * @deprecated use from {@link Lib64SQL#createTable(String, Column...)}
     * <p>
     * Creates a Table, usually used from {@link Lib64SQL#createTable(String, Column...)}
     */
    public Table(Lib64SQL database, String name, List<Column> columns) {
        db = database;
        this.name = name;
        columns.forEach(c -> this.columns.put(c.getName(), c.setNullTable(this)));
    }

    /**
     * Gets the {@link Lib64SQL} database the table is in
     *
     * @return the "parent" database
     */
    public Lib64SQL getDatabase() {
        return db;
    }

    /**
     * Finds and creates the columns not in cache but in the list provided using {@link #addColumn(String, DataType)}
     *
     * @param columns The columns to compare with
     * @return The current table
     */
    public Table matchColumns(Column... columns) {
        for (Column c : columns) {
            if (!this.columns.keySet().contains(c.getName()))
                addColumn(c);
        }
        return this;
    }

    /**
     * Attempts to obtain a cached {@link Column} by the column name
     *
     * @param name the column name
     * @return the column, null if name not found
     */
    public Column getColumn(String name) {
        return columns.get(name);
    }

    /**
     * Gets the name of the table
     *
     * @return table name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets all the cached columns in form of an array
     *
     * @return the array of columns
     */
    public Column[] getColumns() {
        return columns.values().toArray(new Column[0]);
    }

    /**
     * @deprecated
     * @see #addColumn(String, DataType)
     */
    public Table addColumn(Column column) {
        createColumn(column.getName(), column.getType());
        return this;
    }

    /**
     * @see #hasColumn(String)
     */
    public boolean hasColumn(Column column) {
        return hasColumn(column.getName());
    }

    /**
     * Checks to see whether a column with a specific name exists or not
     * <p>
     * does NOT check the type of the column
     *
     * @param name The name to be checked
     * @return <tt>true</tt> if column with that name, case-sensitive, is found, <tt>false</tt> otherwise
     */
    public boolean hasColumn(String name) {
        return columns.containsKey(name);
    }



    /**
     * Adds a column to the current table
     * <p>
     * Libs64 completes this by executing "ALTER TABLE table_name ADD column_name column_type" using {@link Lib64SQL#executeStatement(String)}
     *
     * @param name the name of the Column
     * @param type type of Column data
     * @return The current table
     */
    public Table addColumn(String name, DataType type) {
        createColumn(name, type);
        return this;
    }


    /**
     * non-chain call variant of {@link #addColumn(String, DataType)}
     * @param name name of column to be added
     * @param type type of Column
     * @return the column that exists/created
     */
    public Column createColumn(String name, DataType type){
        Column c = columns.get(name);
        if(c != null)
            return c;

        Column column = new Column(this, name, type);
        if (column.getType() == DataType.UUID && !DataType.isUIDName(column.getName()))
            column.setName(DataType.toUIDName(name));
        String sql = "ALTER TABLE " + name + " ADD " + column.getName() +
                " " + column.getTypeString();
        if (db.executeStatement(sql)) {
            columns.put(column.getName(), column);
            return column;
        }
        return null;
    }

    /**
     * @see #deleteColumn(String)
     */
    public Table deleteColumn(Column column) {
        return deleteColumn(column.getName());
    }

    /**
     * Deletes a specific column
     * <p>
     * Libs64 completes this by executing "ALTER TABLE table_name DROP COLUMN" from db using {@link Lib64SQL#executeStatement(String)}
     *
     * @param name The name of the column
     * @return The current table
     */
    public Table deleteColumn(String name) {
        String sql = "ALTER TABLE " + this.name + " DROP COLUMN " + name;
        if (db.executeStatement(sql))
            columns.remove(name);
        return this;
    }



    public OperationBuilder getOperationBuilder(){
        return new OperationBuilder();
    }

    //TODO Operation by class parse
}
