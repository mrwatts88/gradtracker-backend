package edu.uwm.capstone.model;

import edu.uwm.capstone.security.Authorities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    private String name;
    private String description;
    private Set<Authorities> authorities;

}
