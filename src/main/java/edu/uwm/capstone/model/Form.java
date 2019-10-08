package edu.uwm.capstone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form {

    @ApiModelProperty(hidden = true)
    protected Long id;
    private Long user_id;
    private List<Field> fieldsList;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createdDate;
    @ApiModelProperty(hidden = true)
    private LocalDateTime updatedDate;

    public void addField(Field f) {
        if (this.fieldsList != null)
            this.fieldsList.add(f);
    }

    public boolean removeField(Field f) {
        if (this.fieldsList != null)
            return this.fieldsList.remove(f);
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<Field> getFieldsList() {
        return fieldsList;
    }

    public void setFieldsList(List<Field> fieldsList) {
        this.fieldsList = fieldsList;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
