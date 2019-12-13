package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.model.DegreeProgramState;
import edu.uwm.capstone.util.TestDataUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class DegreeProgramDaoComponentTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DegreeProgramDao degreeProgramDao;

    @Autowired
    DegreeProgramStateDao degreeProgramStateDao;

    private List<DegreeProgram> degreeProgramsToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        // DegreeProgram
        assertNotNull(degreeProgramDao);
        assertNotNull(degreeProgramDao.sql("createDegreeProgram"));
        assertNotNull(degreeProgramDao.sql("readAllDegreePrograms"));
        assertNotNull(degreeProgramDao.sql("readDegreeProgramById"));
        assertNotNull(degreeProgramDao.sql("readDegreeProgramByName"));
        assertNotNull(degreeProgramDao.sql("updateDegreeProgramById"));
        assertNotNull(degreeProgramDao.sql("deleteDegreeProgramById"));

        // DegreeProgramState
        assertNotNull(degreeProgramStateDao);
        assertNotNull(degreeProgramStateDao.sql("createDegreeProgramState"));
        assertNotNull(degreeProgramStateDao.sql("readAllDegreeProgramStates"));
        assertNotNull(degreeProgramStateDao.sql("readDegreeProgramStateById"));
        assertNotNull(degreeProgramStateDao.sql("readDegreeProgramStatesByDegreeProgramId"));
        assertNotNull(degreeProgramStateDao.sql("readDegreeProgramStatesIdsByDegreeProgramId"));
        assertNotNull(degreeProgramStateDao.sql("updateDegreeProgramStateById"));
        assertNotNull(degreeProgramStateDao.sql("deleteDegreeProgramStateById"));
    }

    @After
    public void teardown() {
        degreeProgramsToCleanup.forEach(degreeProgram -> degreeProgramDao.delete(degreeProgram.getId()));
        degreeProgramsToCleanup.clear();
    }

    /**
     * Verify that {@link DegreeProgramDao#create} is working correctly.
     */
    @Test
    public void create() {
        DegreeProgram dp = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanup.add(dp);

        degreeProgramDao.create(dp);
        assertNotNull(dp.getId());
    }

    /**
     * Verify that {@link DegreeProgramDao#create} is working correctly when a request for a {@link DegreeProgram} that
     * contains a value which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createDegreeProgramColumnNameTooLong() {
        DegreeProgram dp = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        dp.setName(TestDataUtility.randomAlphabetic(2000));

        degreeProgramDao.create(dp);
    }

    @Test
    public void read() {
        DegreeProgram dpCreate = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanup.add(dpCreate);
        degreeProgramDao.create(dpCreate);
        assertNotNull(dpCreate.getId());

        DegreeProgram dpRead = degreeProgramDao.read(dpCreate.getId());
        assertNotNull(dpRead);

        assertEquals(dpCreate.getId(), dpRead.getId());
        assertEquals(dpCreate.getName(), dpRead.getName());
        assertEquals(dpCreate.getDescription(), dpRead.getDescription());

        HashMap<Long, DegreeProgramState> map = new HashMap<>();
        dpCreate.forEach((dpState) -> map.put(dpState.getId(), dpState));

        for(DegreeProgramState state : dpRead.getDegreeProgramStates()) {
            assertEquals(map.get(state.getId()).getName(), state.getName());
        }
    }

    @Test
    public void readByName() {
        DegreeProgram dpCreate = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanup.add(dpCreate);
        degreeProgramDao.create(dpCreate);

        assertNotNull(dpCreate.getId());

        DegreeProgram dpRead = degreeProgramDao.readByName(dpCreate.getName());
        assertNotNull(dpRead);

        assertEquals(dpCreate.getId(), dpRead.getId());
        assertEquals(dpCreate.getName(), dpRead.getName());
        assertEquals(dpCreate.getDescription(), dpRead.getDescription());

        HashMap<Long, DegreeProgramState> map = new HashMap<>();
        dpCreate.forEach((dpState) -> map.put(dpState.getId(), dpState));

        for(DegreeProgramState state : dpRead.getDegreeProgramStates()) {
            assertEquals(map.get(state.getId()).getName(), state.getName());
        }
    }

    @Test
    public void readAll() {
        List<DegreeProgram> allPrograms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for(int i = 0; i < randInt; i++) {
            DegreeProgram dp = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
            degreeProgramDao.create(dp);
            degreeProgramsToCleanup.add(dp); // todo: figure out why adding this fails the test due to some SQL constraint
            allPrograms.add(dp);
        }

        HashMap<Long, DegreeProgram> dpHashMap = new HashMap<>();
        allPrograms.forEach((degreeProgram) -> dpHashMap.put(degreeProgram.getId(), degreeProgram));

        for(DegreeProgram program : degreeProgramDao.readAll()) {

            HashMap<Long, DegreeProgramState> map = new HashMap<>();
            dpHashMap.get(program.getId()).forEach((dpState) -> map.put(dpState.getId(), dpState));

            for(DegreeProgramState state : program.getDegreeProgramStates()) {
                assertEquals(map.get(state.getId()).getName(), state.getName());
            }
        }
    }

    @Test
    public void update() {
        // Create a DegreeProgram
        DegreeProgram dpCreate = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanup.add(dpCreate);
        degreeProgramDao.create(dpCreate);

        // Update the DegreeProgram
        DegreeProgram dpUpdate = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        dpUpdate.setId(dpCreate.getId());
        //dpUpdate.setDegreeProgramStates(dpCreate.getDegreeProgramStates());
        degreeProgramDao.update(dpUpdate);

        DegreeProgram dpVerify = degreeProgramDao.read(dpUpdate.getId());
        assertNotNull(dpVerify);

        HashMap<Long, DegreeProgramState> map = new HashMap<>();
        dpUpdate.forEach((dpState) -> map.put(dpState.getId(), dpState));

        for(DegreeProgramState state : dpVerify.getDegreeProgramStates()) {
            assertEquals(map.get(state.getId()).getName(), state.getName());
        }
    }

    @Test
    public void delete() {
        DegreeProgram dpCreate = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramDao.create(dpCreate);
        assertNotNull(dpCreate.getId());

        degreeProgramDao.delete(dpCreate.getId());
    }
}
