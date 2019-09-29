package edu.uwm.capstone.model;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.swagger.annotations.ApiModelProperty;

public class fields {
    protected Long field_id;
    private Long form_id;
    private String label;
    private String field_data;
    private int data_type_flag;
    @ApiModelProperty(hidden = true)
    protected LocalDateTime field_created_date;
    @ApiModelProperty(hidden = true)
    protected LocalDateTime field_updated_date;

    public Long getField_id() {
        return field_id;
    }

    public Long getForm_id() {
        return form_id;
    }

    public String getLabel() {
        return label;
    }

    public String getField_data() {
        return field_data;
    }

    public int getData_type_flag() {
        return data_type_flag;
    }

    public LocalDateTime getField_created_date() {
        return field_created_date;
    }

    public LocalDateTime getField_updated_date() {
        return field_updated_date;
    }

    public void setField_id(Long field_id) {
        this.field_id = field_id;
    }

    public void setForm_id(Long form_id) {
        this.form_id = form_id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setField_data(String field_data) {
        this.field_data = field_data;
    }

    public void setData_type_flag(int data_type_flag) {
        this.data_type_flag = data_type_flag;
    }

    public void setField_created_date(LocalDateTime field_created_date) {
        this.field_created_date = field_created_date;
    }

    public void setField_updated_date(LocalDateTime field_updated_date) {
        this.field_updated_date = field_updated_date;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof fields && (this == object || EqualsBuilder.reflectionEquals(this, object));
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
