package edu.uwm.capstone.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class FormDefinitionTest {

    @Test
    public void testFieldLength() {
        FormDefinition obj = new FormDefinition();
        obj.setFieldDefs(new ArrayList<FieldDefinition>());
        Assert.assertEquals(0, obj.size());

        obj.setFieldDefs(new ArrayList<FieldDefinition>() {{
            add(new FieldDefinition());
        }});
        Assert.assertEquals(1, obj.size());
    }

    @Test
    public void testGetFieldDef() {
        FormDefinition obj = new FormDefinition();
        obj.setFieldDefs(new ArrayList<FieldDefinition>() {{
            add(new FieldDefinition());
        }});

        Assert.assertEquals(null,obj.getFieldDefinitionById(null));


    }
}


