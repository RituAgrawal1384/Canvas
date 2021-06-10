package com.prudential.tap.database;

import java.sql.Connection;
import java.util.Map;

import com.prudential.tap.config.Configvariable;
import com.prudential.tap.config.TapBeansLoad;
import com.prudential.tap.constants.Constants;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

@ComponentScan(basePackages = {"com.prudential.tap"})
@Configuration
public class DataBaseMethodsTest {
    private DatabaseMethods databaseMethods = new DatabaseMethods();
    private Configvariable configvariable;

    @BeforeSuite
    public void setup(){
        TapBeansLoad.setConfigClass(DataBaseMethodsTest.class);
        TapBeansLoad.init();
        configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
        databaseMethods = (DatabaseMethods) TapBeansLoad.getBean(DatabaseMethods.class);
    }

    @Test
    public void testConnectToSqlite() {
        String filePath = "target/test-classes/testFile/RKStorage";
        Connection con = databaseMethods.connectToSqlite(filePath);
        Assert.assertNotNull(con);
    }

    @Test
    public void testSelectFromSqliteDB() {
        String filePath = "target/test-classes/testFile/RKStorage";
        Map<String, String> dataMap = databaseMethods.selectFromSqliteDB(filePath, "SELECT value as FORGOT_PASSWORD FROM catalystLocalStorage WHERE key in (\"persist:forgotPassword\")");
        Assert.assertNotNull(dataMap);
    }


}
