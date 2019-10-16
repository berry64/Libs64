package net.berry64.libs64.sql.operations;

public enum OperationType {
    /* id to sql keyword conversion
    0-SELECT
    1-INSERT
    2-UPDATE
    3-DELETE
     */
    SELECT(0),
    GET(0),
    INSERT(1),
    ADD_ROW(1),
    UPDATE(2),
    CHANGE_DATA(2),
    DELETE(3),
    OTHER(-1);

    static OperationType fromId(int id) {
        switch (id) {
            case 0:
                return SELECT;
            case 1:
                return INSERT;
            case 2:
                return UPDATE;
            case 3:
                return DELETE;
            default:
                return OTHER;
        }
    }

    private final int id;

    OperationType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
