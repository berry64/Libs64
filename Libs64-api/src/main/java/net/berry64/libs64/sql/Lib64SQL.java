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
        return null;
    }

    public static Lib64SQL createSQLite(File file) {
        return null;
    }

    // Abstract definitions
    protected abstract Connection createConnection() throws SQLException;

    public boolean register(Plugin pl, Class<? extends Model> clazz) {
        return true;
    }

    protected void setConnection(Connection connection) {
    }

    protected Connection getRawConnection() {
        return null;
    }

    public PreparedStatement prepareStatement(String sql) {
        return null;
    }

    public <E extends Model> List<E> fetchAll(E incompleteModel) {
        return null;
    }

    public <E extends Model> E fetchOne(E incompleteModel) {
        return null;
    }

    public boolean isRegistered(Class<? extends Model> clazz) {
        return true;
    }

    public boolean addRow(Model model) {
        return true;
    }

    public boolean update(Model model, List<Field> pivot) {
        return true;
    }

    public boolean update(Model model, Field... pivot) {
        return true;
    }

    public boolean update(Model model) {
        return true;
    }

    public boolean update(Model model, String... fieldnames) {
        return true;
    }

    public boolean delete(Model model) {
        return true;
    }
}
