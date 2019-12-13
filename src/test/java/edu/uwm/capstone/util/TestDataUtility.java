package edu.uwm.capstone.util;

import edu.uwm.capstone.model.*;
import edu.uwm.capstone.security.Authorities;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TestDataUtility {

    /**
     * This is a utility class and as such it only has a private constructor.
     */
    protected TestDataUtility() {
    }

    /**
     * Generate a {@link User} object that is fully loaded with random values for testing purposes.
     *
     * @return {@link User}
     */
    public static User userWithTestValues() {
        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(new HashSet<>())
                .authorities(new HashSet<>())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    /**
     * Generate a {@link User} object that is fully loaded with CreateUser power for testing purposes.
     *
     * @return {@link User}
     */

    public static User userWithCreateUserPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithCreateUserPower().getName()))
                .authorities(roleWithCreateUserPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    public static User userWithCreateRolePower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithCreateRolePower().getName()))
                .authorities(roleWithCreateRolePower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    public static User userWithReadAllFormsPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithReadAllFormsPower().getName()))
                .authorities(roleWithReadAllFormsPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    public static User userWithReadAllFormsDefPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithReadAllFormsDefPower().getName()))
                .authorities(roleWithReadAllFormsDefPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    public static User userWithCreateFormDefPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithCreateFormDefPower().getName()))
                .authorities(roleWithCreateFormDefPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    public static User userWithReadAllUsersPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithReadAllUsersPower().getName()))
                .authorities(roleWithReadAllUsersPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    public static User userWithCreateFormPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithCreateFormPower().getName()))
                .authorities(roleWithCreateFormPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithReadAllRolesPower
    public static User userWithReadAllRolesPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithReadAllRolesPower().getName()))
                .authorities(roleWithReadAllRolesPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithUpdateFormDefPower
    public static User userWithUpdateFormDefPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithUpdateFormDefPower().getName()))
                .authorities(roleWithUpdateFormDefPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithApproveFormPower
    public static User userWithApproveFormPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithApproveFormPower().getName()))
                .authorities(roleWithApproveFormPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithUpdateUserPower
    public static User userWithUpdateUserPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithUpdateUserPower().getName()))
                .authorities(roleWithUpdateUserPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithUpdateFormPower
    public static User userWithUpdateFormPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithUpdateFormPower().getName()))
                .authorities(roleWithUpdateFormPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithUpdateRolePower
    public static User userWithUpdateRolePower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithUpdateRolePower().getName()))
                .authorities(roleWithUpdateRolePower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithDeleteFormDefPower
    public static User userWithDeleteFormDefPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithDeleteFormDefPower().getName()))
                .authorities(roleWithDeleteFormDefPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithDeleteFormPower
    public static User userWithDeleteFormPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithDeleteFormPower().getName()))
                .authorities(roleWithDeleteFormPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithDeleteUserPower
    public static User userWithDeleteUserPower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithDeleteUserPower().getName()))
                .authorities(roleWithDeleteUserPower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    //roleWithDeleteRolePower
    public static User userWithDeleteRolePower() {

        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(Collections.singleton(roleWithDeleteRolePower().getName()))
                .authorities(roleWithDeleteRolePower().getAuthorities())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    /**
     * Generate a {@link FormDefinition} object that is fully loaded with random values for testing purposes.
     *
     * @return {@link FormDefinition}
     */
    public static FormDefinition formDefWithTestValues() {
        FormDefinition formDefinition = new FormDefinition();
        formDefinition.setName(randomAlphabetic(randomInt(1, 100)));
        ArrayList<FieldDefinition> fieldDefinitions = new ArrayList<>();
        int j = randomInt(5, 20);
        for (int i = 0; i < j; i++) {
            fieldDefinitions.add(fieldDefWithTestValues());
        }
        formDefinition.setFieldDefs(fieldDefinitions);
        return formDefinition;
    }

    /**
     * Generate a {@link FieldDefinition} object that is fully loaded with random values for testing purposes.
     *
     * @return {@link FieldDefinition}
     */
    public static FieldDefinition fieldDefWithTestValues() {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setLabel(randomAlphabetic(randomInt(1, 50)));
        fieldDefinition.setInputType(randomAlphabetic(randomInt(1, 10))); // this may need to change
        fieldDefinition.setDataType(randomAlphabetic(randomInt(1, 10)));  // this may need to change
        fieldDefinition.setFieldIndex(randomInt());
        return fieldDefinition;
    }

    /**
     * Generate a {@link Form} object that is fully loaded with random values for testing purposes,
     * given a persisted {@link FormDefinition}.
     *
     * @return {@link Form}
     */
    public static Form formWithTestValues(FormDefinition formDefinition, Long userId) {
        Form form = new Form();
        form.setUserId(userId);
        form.setFormDefId(formDefinition.getId());
        ArrayList<Field> fields = new ArrayList<>();
        for (FieldDefinition fd : formDefinition.getFieldDefs()) {
            fields.add(fieldWithTestValues(fd));
        }
        form.setFields(fields);
        return form;
    }

    /**
     * Generate a {@link Field} object that is fully loaded with random values for testing purposes,
     * given a persisted {@link FieldDefinition}.
     *
     * @return {@link Field}
     */
    public static Field fieldWithTestValues(FieldDefinition fieldDefinition) {
        Field field = new Field();
        field.setData(randomAlphabetic(randomInt(10, 200)));
        field.setFieldDefId(fieldDefinition.getId());
        return field;
    }

    /**
     * Generate a {@link Role} object that is fully loaded with random values for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithTestValues() {
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities(randomAuthorities())
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with CreateFormDefPower for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithCreateFormDefPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.CREATE_FORM_DEF);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with CreateFormPower for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithCreateFormPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.CREATE_FORM);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with CREATE_USER Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithCreateUserPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.CREATE_USER);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with CREATE_ROLE Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithCreateRolePower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.CREATE_ROLE);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with READ_ALL_FORMS Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithReadAllFormsPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.READ_ALL_FORMS);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with READ_ALL_FORMS_DEF Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithReadAllFormsDefPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.READ_ALL_FORMS_DEF);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with READ_ALL_USERS Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithReadAllUsersPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.READ_ALL_USERS);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with READ_ALL_ROLES Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithReadAllRolesPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.READ_ALL_ROLES);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with UPDATE_FORM_DEF Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithUpdateFormDefPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.UPDATE_FORM_DEF);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with APPROVE_FORM Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithApproveFormPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.APPROVE_FORM);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with UPDATE_USER Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithUpdateUserPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.UPDATE_USER);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with UPDATE_FORM Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithUpdateFormPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.UPDATE_FORM);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with UPDATE_ROLE Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithUpdateRolePower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.UPDATE_ROLE);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with DELETE_FORM_DEF Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithDeleteFormDefPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.DELETE_FORM_DEF);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with DELETE_FORM Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithDeleteFormPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.DELETE_FORM);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with DELETE_USER Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithDeleteUserPower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.DELETE_USER);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    /**
     * Generate a {@link Role} object that is loaded with DELETE_ROLE Power for testing purposes.
     *
     * @return {@link Role}
     */
    public static Role roleWithDeleteRolePower() {
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        authorities.add(Authorities.DELETE_ROLE);
        return Role.builder()
                .name(randomAlphabetic(randomInt(10, 25)))
                .authorities((Set<Authorities>) authorities)
                .description(randomAlphabetic(randomInt(10, 25)))
                .build();
    }

    public static Set<Authorities> randomAuthorities() {
        return randomAuthorities(randomInt(0, Authorities.values().length));
    }

    /**
     * Generate a random Set of {@link edu.uwm.capstone.security.Authorities} for testing purposes.
     *
     * @param setSize The size of the set to generate, must be in range [0, # of Authorities]
     * @return {@link edu.uwm.capstone.security.Authorities}
     */
    public static Set<Authorities> randomAuthorities(int setSize) {
        assert 0 <= setSize && setSize <= Authorities.values().length;

        Set<Authorities> result = new HashSet<>();
        List<Authorities> authorities = Arrays.asList(Authorities.values());
        Collections.shuffle(authorities);

        Iterator<Authorities> it = authorities.iterator();
        for (int i = 0; i < setSize; i++) {
            result.add(it.next());
        }

        return result;
    }

    public static DegreeProgram degreeProgramWithTestValues(int numStates) {
        assert 1 <= numStates;

        List<DegreeProgramState> degreeProgramStates = new ArrayList<>();
        degreeProgramStates.add(randomDegreeProgramState(true));
        for (int i = 1; i < numStates; i++) {
            degreeProgramStates.add(randomDegreeProgramState(false));
        }

        return DegreeProgram.builder()
                .name(randomAlphabetic(10))
                .description(randomAlphabetic(50))
                .degreeProgramStates(degreeProgramStates)
                .build();
    }

    public static DegreeProgramState randomDegreeProgramState(boolean isInitial) {
        return DegreeProgramState.builder()
                .name(randomAlphabetic(10))
                .description(randomAlphabetic(50))
                .initial(isInitial)
                .build();
    }

    /**
     * Generate a random {@link Boolean}.
     *
     * @return random {@link Boolean}
     */
    public static Boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    /**
     * Generate a random {@link Long} using a minimum value of 1L and a maximum value of {@link Long#MAX_VALUE}.
     *
     * @return random {@link Long}
     */
    public static Long randomLong() {
        return randomLong(1L, Long.MAX_VALUE);
    }

    /**
     * Generate a random {@link Long} using the provided minimum and maximum values.
     *
     * @param min {@link Long} minimum value
     * @param max {@link Long} maximum value
     * @return random {@link Long}
     */
    public static Long randomLong(Long min, Long max) {
        return new Random().longs(min, max).findAny().getAsLong();
    }

    /**
     * Generate a random {@link Integer} using a minimum value of 1 and a maximum value of {@link Integer#MAX_VALUE}.
     *
     * @return random {@link Integer}
     */
    public static int randomInt() {
        return randomInt(1, Integer.MAX_VALUE);
    }

    /**
     * Generate a random {@link int} using the provided minimum and maximum values.
     *
     * @param min {@link int} minimum value
     * @param max {@link int} maximum value
     * @return random {@link int}
     */
    public static int randomInt(int min, int max) {
        return new Random().ints(min, max).findAny().getAsInt();
    }

    /**
     * Generate a {@link String} that contains the provided number of random alphabetic characters.
     *
     * @param characterCount Number of characters
     * @return random {@link String} of alphabetic characters
     */
    public static String randomAlphabetic(int characterCount) {
        return RandomStringUtils.randomAlphabetic(characterCount);
    }

    /**
     * Generate a {@link String} that contains the provided number of random alphanumeric characters.
     *
     * @param characterCount Number of characters
     * @return random {@link String} of alphanumeric characters
     */
    public static String randomAlphanumeric(int characterCount) {
        return RandomStringUtils.randomAlphanumeric(characterCount);
    }

    public static LocalDateTime randomLocalDateTime() {
        LocalDateTime start = LocalDateTime.of(1900, randomMonth(), 1, randomInt(0, 23), randomInt(1, 59));
        long days = ChronoUnit.DAYS.between(start, LocalDateTime.now());
        return start.plusDays(new Random().nextInt((int) days + 1));
    }

    public static Month randomMonth() {
        List<Month> months = Arrays.asList(Month.values());
        int index = new Random().nextInt(months.size());
        return months.get(index);
    }

}
