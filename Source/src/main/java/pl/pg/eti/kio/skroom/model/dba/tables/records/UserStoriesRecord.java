/**
 * This class is generated by jOOQ
 */
package pl.pg.eti.kio.skroom.model.dba.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;

import pl.pg.eti.kio.skroom.model.dba.tables.UserStories;


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
public class UserStoriesRecord extends UpdatableRecordImpl<UserStoriesRecord> implements Record6<Integer, String, Integer, Integer, Integer, Integer> {

    private static final long serialVersionUID = -586651558;

    /**
     * Setter for <code>USER_STORIES.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>USER_STORIES.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>USER_STORIES.NAME</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>USER_STORIES.NAME</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>USER_STORIES.PRIORITY</code>.
     */
    public void setPriority(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>USER_STORIES.PRIORITY</code>.
     */
    public Integer getPriority() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>USER_STORIES.STORY_POINTS</code>.
     */
    public void setStoryPoints(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>USER_STORIES.STORY_POINTS</code>.
     */
    public Integer getStoryPoints() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>USER_STORIES.STATUS</code>.
     */
    public void setStatus(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>USER_STORIES.STATUS</code>.
     */
    public Integer getStatus() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>USER_STORIES.PROJECT_ID</code>.
     */
    public void setProjectId(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>USER_STORIES.PROJECT_ID</code>.
     */
    public Integer getProjectId() {
        return (Integer) get(5);
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
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, Integer, Integer, Integer, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return UserStories.USER_STORIES.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return UserStories.USER_STORIES.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return UserStories.USER_STORIES.PRIORITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return UserStories.USER_STORIES.STORY_POINTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return UserStories.USER_STORIES.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return UserStories.USER_STORIES.PROJECT_ID;
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
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getPriority();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getStoryPoints();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getProjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoriesRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoriesRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoriesRecord value3(Integer value) {
        setPriority(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoriesRecord value4(Integer value) {
        setStoryPoints(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoriesRecord value5(Integer value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoriesRecord value6(Integer value) {
        setProjectId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStoriesRecord values(Integer value1, String value2, Integer value3, Integer value4, Integer value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserStoriesRecord
     */
    public UserStoriesRecord() {
        super(UserStories.USER_STORIES);
    }

    /**
     * Create a detached, initialised UserStoriesRecord
     */
    public UserStoriesRecord(Integer id, String name, Integer priority, Integer storyPoints, Integer status, Integer projectId) {
        super(UserStories.USER_STORIES);

        set(0, id);
        set(1, name);
        set(2, priority);
        set(3, storyPoints);
        set(4, status);
        set(5, projectId);
    }
}
