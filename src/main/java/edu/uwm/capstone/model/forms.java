package edu.uwm.capstone.model;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.swagger.annotations.ApiModelProperty;
public class forms {
    protected Long form_id;
    private Long field_id;
    @ApiModelProperty(hidden = true)
    protected LocalDateTime form_created_date;
    @ApiModelProperty(hidden = true)
    protected LocalDateTime form_updated_date;

    public Long getForm_id() {
        return form_id;
    }

    public Long getField_id() {
        return field_id;
    }

    public LocalDateTime getForm_created_date() {
        return form_created_date;
    }

    public LocalDateTime getForm_updated_date() {
        return form_updated_date;
    }

    public void setForm_id(Long form_id) {
        this.form_id = form_id;
    }

    public void setField_id(Long field_id) {
        this.field_id = field_id;
    }

    public void setForm_created_date(LocalDateTime form_created_date) {
        this.form_created_date = form_created_date;
    }

    public void setForm_updated_date(LocalDateTime form_updated_date) {
        this.form_updated_date = form_updated_date;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof forms && (this == object || EqualsBuilder.reflectionEquals(this, object));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }
}
