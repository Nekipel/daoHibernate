package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    static Connection dbConnection;

    public static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String dbHost = "localhost";
        String dbPort = "3306";
        String dbName = "data";
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        String dbUser = "root";
        String dbPass = "Qwedcxzas123";
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    private static SessionFactory sessionFactory;
    public static SessionFactory getConnectionHibernate() {
        if(sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/data");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "Qwedcxzas123");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                configuration.setProperties(settings).addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e){
                System.out.println("Проблемы с созданием подключения");
                e.printStackTrace();
            } finally {
                if(sessionFactory == null){
                    sessionFactory.close();
                }
            }
        }
        return sessionFactory;
    }
}