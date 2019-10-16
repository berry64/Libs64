package net.berry64.libs64.sql.data;

import java.util.Map;

public abstract class SQLData {
    public abstract Map<String, Object> encode();

    public SQLData(Map<String, Object> data) {

    }
}
