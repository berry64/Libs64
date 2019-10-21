/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql.internal;

import net.berry64.libs64.sql.Model;
import net.berry64.libs64.sql.database.MYSQL;
import net.berry64.libs64.sql.database.SQLITE;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class Lib64SQL {
    // static methods
    public static Lib64SQL createMySQL(String url, int port, String database, String username, String password) {
        return new MYSQL(url, port, database, username, password);
    }

    public static Lib64SQL createSQLite(File file) {
        return new SQLITE(file);
    }

    // Abstract definitions
    protected abstract Connection createConnection() throws SQLException;

    // Methods
    public Map<Class<? extends Model>, ModelData> getRegistry() {
        return registry.registry;
    }

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

    PreparedStatement prepareStatement(String sql) {
        return modelSQL.prepareStatement(sql);
    }

    public <E extends Model> List<E> fetchAll(E incompleteModel) {
        return access.fetchAll(incompleteModel);
    }

    public <E extends Model> E fetchOne(E incompleteModel) {
        return access.fetchOne(incompleteModel);
    }


    //Internal Handlers
    ModelAccess access = new ModelAccess(this);
    ModelRegistry registry = new ModelRegistry(this);
    SQLOperation modelSQL = new SQLOperation(this);
}
