package org.example.hcltech.service;

import org.example.hcltech.entity.Person;
import org.example.hcltech.utils.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseDaoService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String insertQuery="Insert into person(id,name,place,salary) values(?,?,?,?)";
    private static final String selectAllQuery="Select * from person";
    private static final String selectById="Select * from person where id=?";
    private static final String updateById="UPDATE person SET name = ?, place = ?, salary = ? WHERE id = ?";
    private static final String deleteById="Delete from person where id =?";
    public  Person createPerson(Person person) throws SQLException {

        int personId = 0;
        try( Connection connection= DatabaseUtil.getMySqlConnection()){
            if (person != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, person.getId());
                preparedStatement.setString(2, person.getName());
                preparedStatement.setString(3, person.getPlace());
                preparedStatement.setDouble(4, person.getSalary());
                preparedStatement.executeUpdate();
                ResultSet lastInsertedId=preparedStatement.getGeneratedKeys();
                personId=convertResultSetToPesonId(lastInsertedId);
                logger.info("Person Created Successfully");
            }
        }catch(SQLException  e){
            e.printStackTrace();
        }

        return getPersonById(personId).get();
    }
    
    
    private int convertResultSetToPesonId(ResultSet resultSet) throws SQLException {
        if(!resultSet.isClosed()){
            if(resultSet!=null){
               return  resultSet.getInt(1);
            }
        }
        return -1;
    }



    public  List<Person> getAllPersons(){
        try(Connection connection=DatabaseUtil.getMySqlConnection()) {
            PreparedStatement preparedStatement= connection.prepareStatement(selectAllQuery);
            ResultSet resultSet= preparedStatement.executeQuery();
            logger.info("Persons Retrieved Successfully");
            return resultSetToPersonList(resultSet);
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }


    public  Optional<Person> getPersonById(int id){
        try (Connection connection = DatabaseUtil.getMySqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectById)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setPlace(resultSet.getString("place"));
                person.setSalary(resultSet.getDouble("salary"));
                logger.info("Person Retrived Successfully with Id :{}", id);
                return Optional.of(person);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching person by ID", e);
        }
    }



    public  Person updatePersonById(int id,Person person){
        try(Connection connection=DatabaseUtil.getMySqlConnection()){
            Optional<Person> person1=getPersonById(id);
            if(person1.isPresent()) {
                PreparedStatement preparedStatement = connection.prepareStatement(updateById);
                preparedStatement.setString(1, person.getName());
                preparedStatement.setString(2, person.getPlace());
                preparedStatement.setDouble(3, person.getSalary());
                preparedStatement.setInt(4, person.getId());
                preparedStatement.executeUpdate();
                logger.info("Person Updated SuccessFully Id:{}",id);
            }else{
                logger.info("Id :{}Not Found", id);
            }
        }catch (SQLException e){

            throw new RuntimeException();
        }
        return getPersonById(id).get();
    }


    public  boolean deletePersonById(int id){
        try(Connection connection=DatabaseUtil.getMySqlConnection()){
            PreparedStatement preparedStatement=connection.prepareStatement(deleteById);
            preparedStatement.setInt(1,id);
            int res= preparedStatement.executeUpdate();
            if(res==1){
                logger.info("Person Deleted SuccessFully :"+id);
                return true;
            }
        }catch (SQLException e){
            throw new RuntimeException();
        }
        return false;
    }

    private  List<Person> resultSetToPersonList(ResultSet resultSet) throws SQLException {

        List<Person> personList=new ArrayList<>();
        while (resultSet.next()) {
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setPlace(resultSet.getString("place"));
            person.setSalary(resultSet.getDouble("salary"));
            personList.add(person);
        }
        return personList;

    }
}
