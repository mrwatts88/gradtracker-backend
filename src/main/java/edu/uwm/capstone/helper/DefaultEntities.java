package edu.uwm.capstone.helper;

import com.google.common.collect.Sets;
import edu.uwm.capstone.db.DegreeProgramDao;
import edu.uwm.capstone.db.RoleDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.model.DegreeProgramState;
import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.security.Authorities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;

@Component
public class DefaultEntities {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final DegreeProgramDao degreeProgramDao;
    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEntities.class);

    @Autowired
    public DefaultEntities(UserDao userDao, RoleDao roleDao, DegreeProgramDao degreeProgramDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.degreeProgramDao = degreeProgramDao;
        this.passwordEncoder = passwordEncoder;
    }

    public static Role getDefaultAdminRole() {
        return Role.builder()
                .name("Admin")
                .description("The default admin role")
                .authorities(Sets.newHashSet(Authorities.values()))
                .build();
    }

    public static Role getDefaultStudentRole() {
        return Role.builder()
                .name("Student")
                .description("The default student role.")
                .authorities(new HashSet<>())
                .build();
    }

    public static User getDefaultUser() {
        return User.builder()
                .firstName("default_first_name")
                .lastName("default_last_name")
                .email("default@uwm.edu")
                .pantherId("123456789")
                .password("password")
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roleNames(Collections.singleton(getDefaultAdminRole().getName()))
                .build();
    }

    public static DegreeProgram getDefaultDegreeProgram() {
        return DegreeProgram.builder()
                .name("Computer Science")
                .description("The best degree program!")
                .degreeProgramStates(
                        Collections.singletonList(
                                DegreeProgramState.builder()
                                        .name("Application State")
                                        .description("The starting point.")
                                        .initial(true)
                                        .build()
                        )
                ).build();
    }

    public Role persistDefaultAdminRole() {
        Role defaultAdminRole = getDefaultAdminRole();
        LOGGER.info("Persisting default admin role {}", defaultAdminRole);
        return roleDao.create(defaultAdminRole);
    }

    public Role persistDefaultStudentRole() {
        Role studentRole = getDefaultStudentRole();
        LOGGER.info("Persisting default student role {}", studentRole);
        return roleDao.create(studentRole);
    }

    public User persistDefaultUser() {
        User defaultUser = getDefaultUser();
        LOGGER.info("Persisting default user {}", defaultUser);
        defaultUser.setPassword(passwordEncoder.encode(defaultUser.getPassword()));
        return userDao.create(defaultUser);
    }

    public DegreeProgram persistDefaultDegreeProgram() {
        DegreeProgram dp = getDefaultDegreeProgram();
        LOGGER.info("Persisting default degree program {}", dp);
        return degreeProgramDao.create(dp);
    }

    public void persistDefaultEntities() {
        if (roleDao.readByName(getDefaultAdminRole().getName()) == null)
            persistDefaultAdminRole();

        if (roleDao.readByName(getDefaultStudentRole().getName()) == null)
            persistDefaultStudentRole();

        if (userDao.readByEmail(getDefaultUser().getEmail()) == null)
            persistDefaultUser();

        if (degreeProgramDao.readByName(getDefaultDegreeProgram().getName()) == null)
            persistDefaultDegreeProgram();
    }

}
