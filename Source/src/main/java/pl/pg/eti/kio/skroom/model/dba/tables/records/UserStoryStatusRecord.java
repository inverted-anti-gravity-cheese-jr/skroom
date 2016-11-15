/**
 * This class is generated by jOOQ
 */
package pl.pg.eti.kio.skroom.model.dba.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import pl.pg.eti.kio.skroom.model.dba.tables.UserStoryStatus;


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
public class UserStoryStatusRecord extends UpdatableRecordImpl<UserStoryStatusRecord> implements Record4<Integer, String, String, Integer> {

    private static final long serialVersionUID = 609381285;

    /**
     * Setter for <code>USER_STORY_STATUS.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>USER_STORY_STATUS.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>USER_STORY_STATUS.STATUS_NAME</code>.
     */
    public void setStatusName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>USER_STORY_STATUS.STATUS_NAME</code>.
     */
    public String getStatusName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>USER_STORY_STATUS.COLOR</code>.
     */
    public void setColor(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>USER_STORY_STATUS.COLOR</code>.
     */
    public String getColor() {
        return (String) get(2);
    }

    /**
     * Setter for <code>USER_STORY_STATUS.IS_ARCHIVE</code>.
     */
    public void setIsArchive(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>USER_STORY_STATUS.IS_ARCHIVE</code>.
     */
    public Integer getIsArchive() {
        return (Integer) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, String, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<Integer, String, String, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return UserStoryStatus.USER_STORY_STATUS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return UserStoryStatus.USER_STORY_STATUS.STATUS_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return UserStoryStatus.USER_STORY_STATUS.COLOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return UserStoryStatus.USER_STORY_STATUS.IS_ARCHIVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getStatusName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getIsArchive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoryStatusRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoryStatusRecord value2(String value) {
        setStatusName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoryStatusRecord value3(String value) {
        setColor(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoryStatusRecord value4(Integer value) {
        setIsArchive(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoryStatusRecord values(Integer value1, String value2, String value3, Integer value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserStoryStatusRecord
     */
    public UserStoryStatusRecord() {
        super(UserStoryStatus.USER_STORY_STATUS);
    }

    /**
     * Create a detached, initialised UserStoryStatusRecord
     */
    public UserStoryStatusRecord(Integer id, String statusName, String color, Integer isArchive) {
        super(UserStoryStatus.USER_STORY_STATUS);

        set(0, id);
        set(1, statusName);
        set(2, color);
        set(3, isArchive);
    }
}
