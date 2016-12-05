/**
 * This class is generated by jOOQ
 */
package pl.pg.eti.kio.skroom.model.dba.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;

import pl.pg.eti.kio.skroom.model.dba.DefaultSchema;
import pl.pg.eti.kio.skroom.model.dba.Keys;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UsersRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Users extends TableImpl<UsersRecord> {

    private static final long serialVersionUID = -1455104813;

    /**
     * The reference instance of <code>USERS</code>
     */
    public static final Users USERS = new Users();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UsersRecord> getRecordType() {
        return UsersRecord.class;
    }

    /**
     * The column <code>USERS.ID</code>.
     */
    public final TableField<UsersRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>USERS.NAME</code>.
     */
    public final TableField<UsersRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>USERS.EMAIL</code>.
     */
    public final TableField<UsersRecord, String> EMAIL = createField("EMAIL", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>USERS.AVATAR</code>.
     */
    public final TableField<UsersRecord, String> AVATAR = createField("AVATAR", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>USERS.ROLE</code>.
     */
    public final TableField<UsersRecord, Integer> ROLE = createField("ROLE", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>USERS</code> table reference
     */
    public Users() {
        this("USERS", null);
    }

    /**
     * Create an aliased <code>USERS</code> table reference
     */
    public Users(String alias) {
        this(alias, USERS);
    }

    private Users(String alias, Table<UsersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Users(String alias, Table<UsersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UsersRecord> getPrimaryKey() {
        return Keys.PK_USERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UsersRecord>> getKeys() {
        return Arrays.<UniqueKey<UsersRecord>>asList(Keys.PK_USERS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Users as(String alias) {
        return new Users(alias, this);
    }

    /**
     * Rename this table
     */
    public Users rename(String name) {
        return new Users(name, null);
    }
}
