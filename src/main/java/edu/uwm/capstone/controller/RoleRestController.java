package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.security.Authorities;
import edu.uwm.capstone.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class RoleRestController {

    static final String ROLE_PATH = "/role/";
    static final String READ_ROLE_BY_NAME_PATH = ROLE_PATH + "name/";
    static final String ROLE_AUTHORITIES = ROLE_PATH + "authorities/";

    private static final Logger LOG = LoggerFactory.getLogger(RoleRestController.class);

    private final RoleService roleService;

    @Autowired
    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Creates the provided {@link Role}.
     *
     * @param role     {@link Role} to create
     * @param response {@link HttpServletResponse} that is sent back
     * @return created {@link Role}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Create Role")
    @PostMapping(value = ROLE_PATH)
    public Role create(@RequestBody Role role, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> roleService.create(role), response, LOG);
    }

    /**
     * Get a {@link Role} by its Id.
     *
     * @param roleId   id of the {@link Role} to read
     * @param response {@link HttpServletResponse} that is sent back
     * @return {@link Role} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read Role by ID")
    @GetMapping(value = ROLE_PATH + "{roleId}")
    public Role readById(@PathVariable Long roleId, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> roleService.read(roleId), response, LOG);
    }

    /**
     * Get a {@link Role} by its Name.
     *
     * @param roleName name of the {@link Role} to read
     * @param response {@link HttpServletResponse} that is sent back
     * @return {@link Role} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read Role by Name")
    @GetMapping(value = READ_ROLE_BY_NAME_PATH + "/{roleName}")
    public Role readByName(@PathVariable String roleName, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> roleService.readByName(roleName), response, LOG);
    }

    /**
     * Gets all the {@link Role}s
     *
     * @return list of {@link Role}s retrieved from the database
     */
    @ApiOperation(value = "Read All Roles")
    @GetMapping(value = ROLE_PATH)
    public List<Role> readAll() {
        return roleService.readAll();
    }

    /**
     * Gets all the {@link Authorities}s
     *
     * @return list of {@link Authorities}s
     */
    @ApiOperation(value = "Read All Authorities")
    @GetMapping(value = ROLE_AUTHORITIES)
    public List<Authorities> readAllAuthorities() {
        return Arrays.asList(Authorities.values());
    }

    /**
     * Updates a {@link Role} by its Id.
     *
     * @param roleId   id of the {@link Role} to update
     * @param role     updated {@link Role}
     * @param response {@link HttpServletResponse} that is sent back
     * @return updated {@link Role}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Update Role by ID")
    @PutMapping(value = ROLE_PATH + "{roleId}")
    public Role update(@PathVariable Long roleId, @RequestBody Role role, @ApiIgnore HttpServletResponse response) throws IOException {
        role.setId(roleId);
        return RestControllerUtil.runCallable(() -> roleService.update(role), response, LOG);
    }

    /**
     * Deletes a {@link Role} by Id.
     *
     * @param roleId   id of the {@link Role} to update
     * @param response {@link HttpServletResponse} that is sent back
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Delete Role by ID")
    @DeleteMapping(value = ROLE_PATH + "{roleId}")
    public void deleteById(@PathVariable Long roleId, @ApiIgnore HttpServletResponse response) throws IOException {
        RestControllerUtil.runRunnable(() -> roleService.delete(roleId), response, LOG);
    }
}
