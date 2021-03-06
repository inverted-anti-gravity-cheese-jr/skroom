/**
 * This class is generated by jOOQ
 */
package pl.pg.eti.kio.skroom.model.dba.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;

import pl.pg.eti.kio.skroom.model.dba.DefaultSchema;
import pl.pg.eti.kio.skroom.model.dba.Keys;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UserStoryStatusRecord;


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
public class UserStoryStatus extends TableImpl<UserStoryStatusRecord> {

    private static final long serialVersionUID = 912228733;

    /**
     * The reference instance of <code>USER_STORY_STATUS</code>
     */
    public static final UserStoryStatus USER_STORY_STATUS = new UserStoryStatus();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserStoryStatusRecord> getRecordType() {
        return UserStoryStatusRecord.class;
    }

    /**
     * The column <code>USER_STORY_STATUS.ID</code>.
     */
    public final TableField<UserStoryStatusRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>USER_STORY_STATUS.STATUS_NAME</code>.
     */
    public final TableField<UserStoryStatusRecord, String> STATUS_NAME = createField("STATUS_NAME", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>USER_STORY_STATUS.COLOR</code>.
     */
    public final TableField<UserStoryStatusRecord, String> COLOR = createField("COLOR", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>USER_STORY_STATUS.IS_ARCHIVE</code>.
     */
    public final TableField<UserStoryStatusRecord, Integer> IS_ARCHIVE = createField("IS_ARCHIVE", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>USER_STORY_STATUS</code> table reference
     */
    public UserStoryStatus() {
        this("USER_STORY_STATUS", null);
    }

    /**
     * Create an aliased <code>USER_STORY_STATUS</code> table reference
     */
    public UserStoryStatus(String alias) {
        this(alias, USER_STORY_STATUS);
    }

    private UserStoryStatus(String alias, Table<UserStoryStatusRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserStoryStatus(String alias, Table<UserStoryStatusRecord> aliased, Field<?>[] parameters) {
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
    public Identity<UserStoryStatusRecord, Integer> getIdentity() {
        return Keys.IDENTITY_USER_STORY_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UserStoryStatusRecord> getPrimaryKey() {
        return Keys.PK_USER_STORY_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UserStoryStatusRecord>> getKeys() {
        return Arrays.<UniqueKey<UserStoryStatusRecord>>asList(Keys.PK_USER_STORY_STATUS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoryStatus as(String alias) {
        return new UserStoryStatus(alias, this);
    }

    /**
     * Rename this table
     */
    public UserStoryStatus rename(String name) {
        return new UserStoryStatus(name, null);
    }
}
