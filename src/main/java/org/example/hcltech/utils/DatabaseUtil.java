package org.example.hcltech.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    private static HikariDataSource mySqlDataSource=null;

    private static HikariDataSource getMySqlDataSource(){

        if(mySqlDataSource==null) {
            try {
                logger.info("Initializing HikariCP DataSource...");
                HikariConfig hikariConfig = new HikariConfig("/hikariconfig");
                mySqlDataSource = new HikariDataSource(hikariConfig);
                logger.info("HikariCP DataSource initialized successfully.");
            } catch (Exception e) {
                logger.error("Error initializing HikariCP DataSource: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to initialize HikariCP DataSource", e);
            }
        }
        return mySqlDataSource;
    }

    public static Connection getMySqlConnection(){
        try {
            return getMySqlDataSource().getConnection();
        } catch (SQLException e) {
            logger.error("Error obtaining database connection: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    public static void closeDataSource() {
        if (mySqlDataSource != null) {
            logger.info("Closing HikariCP DataSource...");
            mySqlDataSource.close();
            logger.info("HikariCP DataSource closed successfully.");
        }
    }
}
