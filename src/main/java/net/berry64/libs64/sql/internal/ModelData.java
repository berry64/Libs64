/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql.internal;

import net.berry64.libs64.sql.Model;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;

public class ModelData {
    Class<? extends Model> clazz = null;
    PreparedStatement insertStatement = null;
    List<Field> affectedColumns = null;
    String tableName = null;
    Plugin plugin = null;

    ModelData() {}
}
