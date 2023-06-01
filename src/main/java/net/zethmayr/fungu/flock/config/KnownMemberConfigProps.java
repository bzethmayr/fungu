package net.zethmayr.fungu.flock.config;

/**
 * An anemic implementation of a known member configuration.
 */
public class KnownMemberConfigProps implements ConfiguresKnownMember {

    /**
     * Returns a new mutable instance with null values.
     */
    public KnownMemberConfigProps() {

    }

    private String location;
    private Integer index;

    private Long initialValue;

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Integer getIndex() {
        return index;
    }

    @Override
    public void setLocation(final String location) {
        this.location = location;
    }

    @Override
    public void setIndex(final Integer index) {
        this.index = index;
    }

    @Override
    public Long getInitialValue() {
        return initialValue;
    }

    @Override
    public void setInitialValue(final Long initialValue) {
        this.initialValue = initialValue;
    }
}
