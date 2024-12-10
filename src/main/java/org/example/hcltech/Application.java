package org.example.hcltech;


import org.example.hcltech.entity.Person;
import org.example.hcltech.service.DatabaseDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) throws SQLException {
        logger.info("Database Management Application started");
        Application application=new Application();
         application. go();
    }


    private  void go(){

        DatabaseDaoService databaseService=new DatabaseDaoService();
        List<Person> personList=createPersonList();

        //Inserting the personList into database
        personList.stream()
                .map(person -> {
                    try {
                        return databaseService.createPerson(person);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error creating person: " + person, e);
                    }
                })
                .forEach(System.out::println);

        //Retrieving all the persons from database
        databaseService.getAllPersons().stream()
                .forEach(System.out::println);

        //Retreiving the person by id
       Optional<Person> person= databaseService.getPersonById(2);
       System.out.println(person.get());

       //Updating the person by id
        Person p3=new Person();
        p3.setId(3);
        p3.setName("Abirami");
        p3.setPlace("Madurai");
        p3.setSalary(2000);
       Person person1= databaseService.updatePersonById(1,p3);
       System.out.println(person1);

       //Delete the person by id
        boolean b = databaseService.deletePersonById(3);
        System.out.println(b);

    }



    public static List<Person> createPersonList(){
        List<Person> personList=new ArrayList<>();
        Person p1=new Person();
        p1.setId(1);
        p1.setName("Mohan");
        p1.setPlace("Melur");
        p1.setSalary(10000);

        Person p2=new Person();
        p2.setId(2);
        p2.setName("Gowtham");
        p2.setPlace("Chennai");
        p2.setSalary(20000);

        Person p3=new Person();
        p3.setId(3);
        p3.setName("AbiramiMohan");
        p3.setPlace("Thanajvur");
        p3.setSalary(30000);

        personList.add(p1);
        personList.add(p2);
        personList.add(p3);
        return personList;
    }
}