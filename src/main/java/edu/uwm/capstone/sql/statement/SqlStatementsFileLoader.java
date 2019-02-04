package edu.uwm.capstone.sql.statement;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SqlStatementsFileLoader implements ISqlStatementsFileLoader, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlStatementsFileLoader.class);

    private static final int READ_STREAM_CHARACTERS = 65536;

    private Map<String, String> statements = new HashMap<>();

    private SqlStatementsFileParser parser = new SqlStatementsFileParser();

    private String statementResourceLocation;

    /**
     * Obtain the requested SQL statement from the {@link #statements} that have been initialized by
     * {@link #setApplicationContext(ApplicationContext)} and {@link #setStatementResourceLocation(String)}.
     * @param name String name of the specific sql statement being requested
     * @return String value of the specific sql statement being requested
     */
    @Override
    public String sql(String name) {
        String statement = statements.get(name);
        if (statement == null) {
            throw new SqlStatementsMissingException("No SQL statement with name " + name + " found in store");
        }
        return statement;
    }

    private String readStream(InputStream stream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char[] cbuf = new char[READ_STREAM_CHARACTERS];
        InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF-8"));
        while (reader.ready()) {
            reader.read(cbuf);
            stringBuilder.append(new String(cbuf));
        }
        return stringBuilder.toString();
    }

    /**
     * This method is initialized via the {@link ApplicationContextAware} implementation.
     * @param applicationContext {@link ApplicationContext}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(classLoader);
            Resource[] resources = resourcePatternResolver.getResources(statementResourceLocation);
            for (Resource resource : resources) {
                try {
                    LOGGER.info("Loading sql statements from {}", resource.getFilename());
                    String fileContents = readStream(resource.getInputStream());
                    Map<String, String> sqlStatements = parser.parse(fileContents);
                    if (LOGGER.isDebugEnabled()) {
                        for (String name : sqlStatements.keySet()) {
                            LOGGER.debug("Statement {} : {}", name, sqlStatements.get(name));
                        }
                    }
                    this.statements.putAll(sqlStatements);
                } catch (IOException e) {
                    throw new SqlStatementsFileResourceException("Error while reading sql resource file " + resource.getFilename(), e);
                } catch (SqlStatementsFileParseException e) {
                    throw new SqlStatementsFileResourceException("Error parsing sql resource file " + resource.getFilename(), e);
                }
            }
        } catch (IOException e) {
            throw new SqlStatementsFileResourceException("Error locating sql resource files in classpath", e);
        }
    }

    @Override
    public String getStatementResourceLocation() {
        return statementResourceLocation;
    }

    @Override
    public void setStatementResourceLocation(String statementResourceLocation) {
        this.statementResourceLocation = statementResourceLocation;
    }

}
