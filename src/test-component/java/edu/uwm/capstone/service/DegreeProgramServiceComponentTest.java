package edu.uwm.capstone.service;

import edu.uwm.capstone.Application;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.util.TestDataUtility;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DegreeProgramServiceComponentTest {
    @Autowired
    private DegreeProgramService degreeProgramService;

    private List<DegreeProgram> degreeProgramsToCleanUp = new ArrayList<>();

    @Before
    public void setUp() {
        assertNotNull(degreeProgramService);
    }

    @After
    public void teardown() {
        degreeProgramsToCleanUp.forEach(degreeProgram -> degreeProgramService.delete(degreeProgram.getId()));
        degreeProgramsToCleanUp.clear();
    }

    /**
     * Verify that {@link DegreeProgramService#create(DegreeProgram)} is working correctly.
     */
    @Test
    public void create() {
        DegreeProgram degreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanUp.add(degreeProgram);

        degreeProgramService.create(degreeProgram);
        assertNotNull(degreeProgram.getId());
        assertNotNull(degreeProgram.getCreatedDate());

        DegreeProgram verifyFormDef = degreeProgramService.read(degreeProgram.getId());
        assertEquals(degreeProgram, verifyFormDef);
    }

    /**
     * Verify that {@link DegreeProgramService#create(DegreeProgram)} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNullDegreeProgram() {
        degreeProgramService.create(null);
    }

    /**
     * Verify that {@link DegreeProgramService#create(DegreeProgram)} is working correctly when a request for a {@link DegreeProgram} with a non-null id is made.
     */
    @Test(expected = RuntimeException.class)
    public void createNonNullDegreeProgram() {
        DegreeProgram degreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgram.setId(TestDataUtility.randomLong());
        degreeProgramService.create(degreeProgram);
    }

    /**
     * Verify that {@link DegreeProgramService#create(DegreeProgram)} is working correctly when a request for a {@link DegreeProgram} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void createDegreeProgramNameColumnTooLong() {
        DegreeProgram degreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgram.setName(RandomStringUtils.randomAlphabetic(2000));
        degreeProgramService.create(degreeProgram);
    }

    /**
     * Verify that {@link DegreeProgramService#read(Long)} is working correctly.
     */
    @Test
    public void read() {
        DegreeProgram degreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanUp.add(degreeProgram);

        degreeProgramService.create(degreeProgram);
        assertNotNull(degreeProgram.getId());

        DegreeProgram readDegreeProgram = degreeProgramService.read(degreeProgram.getId());
        assertNotNull(readDegreeProgram);
        assertEquals(degreeProgram, readDegreeProgram);
    }

    /**
     * Verify that {@link DegreeProgramService#read(Long)} is working correctly when a request for a non-existent {@link DegreeProgram #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void readNonExistentDegreeProgram() {
        Long id = TestDataUtility.randomLong();
        degreeProgramService.read(id);
    }

    /**
     * Verify that all {@link DegreeProgramService#readAll()} is working correctly,
     */
    @Test
    public void readAll() {
        List<DegreeProgram> persistedDegreePrograms = new ArrayList<>();
        int randInt = TestDataUtility.randomInt(10, 30);
        for (int i = 0; i < randInt; i++) {
            DegreeProgram degreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
            degreeProgramService.create(degreeProgram);
            degreeProgramsToCleanUp.add(degreeProgram);
            persistedDegreePrograms.add(degreeProgram);
        }
        assertEquals(persistedDegreePrograms, degreeProgramService.readAll());
    }

    /**
     * Verify that {@link DegreeProgramService#update(DegreeProgram)} is working correctly.
     */
    @Test
    public void update() {
        DegreeProgram createDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanUp.add(createDegreeProgram);

        degreeProgramService.create(createDegreeProgram);
        assertNotNull(createDegreeProgram.getId());

        DegreeProgram verifyCreateDegreeProgram = degreeProgramService.read(createDegreeProgram.getId());
        assertNotNull(verifyCreateDegreeProgram);
        assertEquals(createDegreeProgram, verifyCreateDegreeProgram);

        DegreeProgram updateDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        updateDegreeProgram.setId(createDegreeProgram.getId());
        degreeProgramService.update(updateDegreeProgram);

        DegreeProgram verifyUpdateDegreeProgram = degreeProgramService.read(updateDegreeProgram.getId());
        assertNotNull(verifyUpdateDegreeProgram);
        assertEquals(updateDegreeProgram, verifyUpdateDegreeProgram);
        assertNotEquals(verifyUpdateDegreeProgram, verifyCreateDegreeProgram);
    }

    /**
     * Verify that {@link DegreeProgramService#update(DegreeProgram)} is working correctly when a request for creating a null object is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNullDegreeProgram() {
        degreeProgramService.update(null);
    }

    /**
     * Verify that {@link DegreeProgramService#update(DegreeProgram)} is working correctly when a request for a non-existent {@link DegreeProgram #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateNonExistentDegreeProgram() {
        DegreeProgram updateDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        updateDegreeProgram.setId(TestDataUtility.randomLong());
        degreeProgramService.update(updateDegreeProgram);
    }

    /**
     * Verify that {@link DegreeProgramService#update(DegreeProgram)} is working correctly when a request for a {@link DegreeProgram} that contains a value
     * which exceeds the database configuration is made.
     */
    @Test(expected = RuntimeException.class)
    public void updateDegreeProgramColumnTooLong() {
        DegreeProgram createDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanUp.add(createDegreeProgram);

        degreeProgramService.create(createDegreeProgram);
        assertNotNull(createDegreeProgram.getId());

        DegreeProgram updateDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        updateDegreeProgram.setId(createDegreeProgram.getId());
        updateDegreeProgram.setName(RandomStringUtils.randomAlphabetic(2000));
        degreeProgramService.update(updateDegreeProgram);
    }

    /**
     * Verify that {@link DegreeProgramService#update(DegreeProgram)} is working correctly when field definitions are updated
     * but not exist for form definition.
     */
    @Test(expected = RuntimeException.class)
    public void updateDegreeProgramUnknownDps() {
        DegreeProgram createDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanUp.add(createDegreeProgram);

        degreeProgramService.create(createDegreeProgram);
        assertNotNull(createDegreeProgram.getId());

        DegreeProgram updateDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        updateDegreeProgram.setId(TestDataUtility.randomLong());

        degreeProgramService.update(updateDegreeProgram);
    }

    /**
     * Verify that {@link DegreeProgramService#update(DegreeProgram)} is working correctly when field definitions are updated
     * but without FieldDef.
     */
    @Test(expected = RuntimeException.class)
    public void updateDegreeProgramEmptyDps() {
        DegreeProgram createFormDef = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramsToCleanUp.add(createFormDef);

        degreeProgramService.create(createFormDef);
        assertNotNull(createFormDef.getId());

        DegreeProgram updateFormDef = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        updateFormDef.setId(createFormDef.getId());
        updateFormDef.setDegreeProgramStates(Collections.emptyList());
        degreeProgramService.update(updateFormDef);
    }

    /**
     * Verify that {@link DegreeProgramService#delete(Long)} is working correctly.
     */
    @Test
    public void delete() {
        DegreeProgram createDegreeProgram = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(1, 10));
        degreeProgramService.create(createDegreeProgram);
        assertNotNull(createDegreeProgram.getId());

        degreeProgramService.delete(createDegreeProgram.getId());
    }

    /**
     * Verify that {@link DegreeProgramService#delete(Long)} is working correctly when a request for a non-existent {@link DegreeProgram #id} is made.
     */
    @Test(expected = RuntimeException.class)
    public void deleteNonExistentDegreeProgram() {
        Long id = TestDataUtility.randomLong();
        degreeProgramService.delete(id);
        assertNull(degreeProgramService.readAll());
    }
}