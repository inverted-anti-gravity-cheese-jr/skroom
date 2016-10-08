/**
 * This class is generated by jOOQ
 */
package pl.pg.eti.kio.skroom.model.dba.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;

import pl.pg.eti.kio.skroom.model.dba.DefaultSchema;
import pl.pg.eti.kio.skroom.model.dba.Keys;
import pl.pg.eti.kio.skroom.model.dba.tables.records.UserStoriesRecord;


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
public class UserStories extends TableImpl<UserStoriesRecord> {

    private static final long serialVersionUID = 1872265301;

    /**
     * The reference instance of <code>USER_STORIES</code>
     */
    public static final UserStories USER_STORIES = new UserStories();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserStoriesRecord> getRecordType() {
        return UserStoriesRecord.class;
    }

    /**
     * The column <code>USER_STORIES.ID</code>.
     */
    public final TableField<UserStoriesRecord, Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>USER_STORIES.NAME</code>.
     */
    public final TableField<UserStoriesRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>USER_STORIES.PRIORITY</code>.
     */
    public final TableField<UserStoriesRecord, Integer> PRIORITY = createField("PRIORITY", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>USER_STORIES.STORY_POINTS</code>.
     */
    public final TableField<UserStoriesRecord, Integer> STORY_POINTS = createField("STORY_POINTS", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>USER_STORIES.STATUS</code>.
     */
    public final TableField<UserStoriesRecord, Integer> STATUS = createField("STATUS", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>USER_STORIES.PROJECT_ID</code>.
     */
    public final TableField<UserStoriesRecord, Integer> PROJECT_ID = createField("PROJECT_ID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>USER_STORIES</code> table reference
     */
    public UserStories() {
        this("USER_STORIES", null);
    }

    /**
     * Create an aliased <code>USER_STORIES</code> table reference
     */
    public UserStories(String alias) {
        this(alias, USER_STORIES);
    }

    private UserStories(String alias, Table<UserStoriesRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserStories(String alias, Table<UserStoriesRecord> aliased, Field<?>[] parameters) {
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
    public Identity<UserStoriesRecord, Integer> getIdentity() {
        return Keys.IDENTITY_USER_STORIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UserStoriesRecord> getPrimaryKey() {
        return Keys.PK_USER_STORIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UserStoriesRecord>> getKeys() {
        return Arrays.<UniqueKey<UserStoriesRecord>>asList(Keys.PK_USER_STORIES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<UserStoriesRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<UserStoriesRecord, ?>>asList(Keys.FK_USER_STORIES_PROJECTS_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStories as(String alias) {
        return new UserStories(alias, this);
    }

    /**
     * Rename this table
     */
    public UserStories rename(String name) {
        return new UserStories(name, null);
    }
}
