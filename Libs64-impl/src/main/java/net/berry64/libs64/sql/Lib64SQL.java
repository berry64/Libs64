/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import net.berry64.libs64.sql.database.MYSQL;
import net.berry64.libs64.sql.database.SQLITE;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class Lib64SQL {
    // static methods, I wish java had a header file?
    public static Lib64SQL createMySQL(String url, int port, String database, String username, String password) {
        return new MYSQL(url, port, database, username, password);
    }

    public static Lib64SQL createSQLite(File file) {
        return new SQLITE(file);
    }

    // Abstract definitions
    protected abstract Connection createConnection() throws SQLException;

    //exported functions
    boolean checkConnection() {
        return modelSQL.checkConnection();
    }

    public boolean register(Plugin pl, Class<? extends Model> clazz) {
        return registry.register(pl, clazz);
    }

    protected void setConnection(Connection connection) {
        modelSQL.connection = connection;
    }

    protected Connection getRawConnection() {
        return modelSQL.connection;
    }

    public PreparedStatement prepareStatement(String sql) {
        return modelSQL.prepareStatement(sql);
    }

    public <E extends Model> List<E> fetchAll(E incompleteModel) {
        return access.fetchAll(incompleteModel);
    }

    public <E extends Model> E fetchOne(E incompleteModel) {
        return access.fetchOne(incompleteModel);
    }

    public boolean isRegistered(Class<? extends Model> clazz) {
        return registry.isRegistered(clazz);
    }

    public boolean addRow(Model model) {
        return registry.addRow(model);
    }

    public boolean update(Model model, List<Field> pivot) {
        return access.update(model, pivot);
    }

    public boolean update(Model model, Field... pivot) {
        return access.update(model, pivot);
    }

    public boolean update(Model model) {
        return access.update(model);
    }

    public boolean update(Model model, String... fieldnames) {
        return access.update(model, fieldnames);
    }

    public boolean delete(Model model) {
        return access.delete(model);
    }

    //Internal Handlers
    ModelAccess access = new ModelAccess(this);
    ModelRegistry registry = new ModelRegistry(this);
    SQLOperation modelSQL = new SQLOperation(this);
}
