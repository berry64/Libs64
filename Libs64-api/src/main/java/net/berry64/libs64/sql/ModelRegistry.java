package net.berry64.libs64.sql;

import net.berry64.libs64.sql.database.SQLITE;
import net.berry64.libs64.sql.exceptions.ConnectionFailedException;
import net.berry64.libs64.sql.exceptions.InvalidModelException;
import org.bukkit.plugin.Plugin;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import static net.berry64.libs64.sql.DataType.setAnnotationDataTypeField;

