package com.thinking.machines.hr.dl;
import java.util.*;
import java.sql.*;
import java.math.*;
import java.text.*;
public class EmployeeDAO
{
public EmployeeDTO deleteByEmployeeId(String employeeId) throws DAOException //returns name of employee deleted in DTO
{
if(employeeId.charAt(0) == 'A') employeeId = employeeId.substring(1);
int actualEmployeeId = Integer.parseInt(employeeId);
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT name FROM employee WHERE id=?");
prepareStatement.setInt(1,actualEmployeeId);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("Invalid employee Id: "+employeeId);
}
String name = resultSet.getString("name");
resultSet.close();
prepareStatement.close();
prepareStatement = connection.prepareStatement("DELETE FROM employee WHERE id=?");
prepareStatement.setInt(1,actualEmployeeId);
prepareStatement.executeUpdate();
prepareStatement.close();
connection.close();
EmployeeDTO employeeDTO = new EmployeeDTO();
employeeDTO.setName(name);
return employeeDTO;
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public void update(EmployeeDTO employeeDTO) throws DAOException
{
String employeeIdString = employeeDTO.getEmployeeId();
if(employeeIdString.charAt(0) == 'A') employeeIdString = employeeIdString.substring(1);
int employeeId = Integer.parseInt(employeeIdString);
String panNumber = employeeDTO.getPANNumber();
String aadharCardNumber = employeeDTO.getAadharCardNumber();
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT id FROM employee WHERE pan_number=? AND id!=?");
prepareStatement.setString(1,panNumber);
prepareStatement.setInt(2,employeeId);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("PAN number: "+panNumber+" already exists.");
}
resultSet.close();
prepareStatement.close();
prepareStatement = connection.prepareStatement("SELECT id FROM employee WHERE aadhar_card_number=? AND id!=?");
prepareStatement.setString(1,aadharCardNumber);
prepareStatement.setInt(2,employeeId);
resultSet = prepareStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("Aadhar card number: "+aadharCardNumber+" already exists.");
}
resultSet.close();
prepareStatement.close();
String name = employeeDTO.getName();
int designationCode = employeeDTO.getDesignationCode();
java.util.Date dateOfBirth = employeeDTO.getDateOfBirth();
String gender = employeeDTO.getGender();
boolean isIndian = employeeDTO.getIsIndian();
BigDecimal basicSalary = employeeDTO.getBasicSalary();

prepareStatement = connection.prepareStatement("UPDATE employee SET name=?,designation_code=?,date_of_birth=?,gender=?,is_indian=?,basic_salary=?,pan_number=?,aadhar_card_number=? WHERE id=?");
prepareStatement.setString(1,name);
prepareStatement.setInt(2,designationCode);
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
java.sql.Date sqlDateOfBirth = new java.sql.Date(dateOfBirth.getTime());
prepareStatement.setDate(3,sqlDateOfBirth); 
prepareStatement.setString(4,gender);
prepareStatement.setBoolean(5,isIndian);
prepareStatement.setBigDecimal(6,basicSalary);
prepareStatement.setString(7,panNumber);
prepareStatement.setString(8,aadharCardNumber);
prepareStatement.setInt(9,employeeId);

prepareStatement.executeUpdate();
prepareStatement.close();
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public EmployeeDTO getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId.charAt(0) == 'A') employeeId = employeeId.substring(1); //to convert A100001 to 100001
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT employee.id,employee.name,employee.designation_code,designation.title,employee.date_of_birth,employee.gender,employee.is_indian,employee.basic_salary,employee.pan_number,employee.aadhar_card_number FROM employee INNER JOIN designation ON employee.designation_code=designation.code WHERE id=?");
prepareStatement.setString(1,employeeId);
ResultSet resultSet;
resultSet = prepareStatement.executeQuery();
if(resultSet.next()==false)
{ 
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("Invalid employee id");
}

int employeeIdInt = resultSet.getInt("id");
String name = resultSet.getString("name").trim();
int designationCode = resultSet.getInt("designation_code");
String title = resultSet.getString("title").trim();
java.sql.Date dateOfBirth = resultSet.getDate("date_of_birth");
String gender = resultSet.getString("gender");
boolean isIndian = resultSet.getBoolean("is_indian");
BigDecimal basicSalary = resultSet.getBigDecimal("basic_salary");
String panNumber = resultSet.getString("pan_number").trim();
String aadharCardNumber = resultSet.getString("aadhar_card_number").trim();
EmployeeDTO employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId("A" + employeeIdInt);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setTitle(title);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean designationCodeExists(int designationCode) throws DAOException
{
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT code FROM designation WHERE code=?");
prepareStatement.setInt(1,designationCode);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next()) return true;
else return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean panNumberExists(String panNumber) throws DAOException
{
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT gender FROM employee WHERE pan_number=?");
prepareStatement.setString(1,panNumber);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next()) return true;
else return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT gender FROM employee WHERE aadhar_card_number=?");
prepareStatement.setString(1,aadharCardNumber);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next()) return true;
else return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public void add(EmployeeDTO employeeDTO) throws DAOException
{
String panNumber = employeeDTO.getPANNumber();
String aadharCardNumber = employeeDTO.getAadharCardNumber();
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT id FROM employee WHERE pan_number=?");
prepareStatement.setString(1,panNumber);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("PAN number: "+panNumber+" already exists.");
}
resultSet.close();
prepareStatement.close();
prepareStatement = connection.prepareStatement("SELECT id FROM employee WHERE aadhar_card_number=?");
prepareStatement.setString(1,aadharCardNumber);
resultSet = prepareStatement.executeQuery();
if(resultSet.next())
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("Aadhar card number: "+aadharCardNumber+" already exists.");
}
resultSet.close();
prepareStatement.close();
String name = employeeDTO.getName();
int designationCode = employeeDTO.getDesignationCode();
java.util.Date dateOfBirth = employeeDTO.getDateOfBirth();
String gender = employeeDTO.getGender();
boolean isIndian = employeeDTO.getIsIndian();
BigDecimal basicSalary = employeeDTO.getBasicSalary();

prepareStatement = connection.prepareStatement("INSERT INTO employee (name,designation_code,date_of_birth,gender,is_indian,basic_salary,pan_number,aadhar_card_number) VALUES (?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
prepareStatement.setString(1,name);
prepareStatement.setInt(2,designationCode);
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
java.sql.Date sqlDateOfBirth = new java.sql.Date(dateOfBirth.getTime());
prepareStatement.setDate(3,sqlDateOfBirth);  		//won't accept java.util.Date. It wants java.sql.Date.
prepareStatement.setString(4,gender);
prepareStatement.setBoolean(5,isIndian);
prepareStatement.setBigDecimal(6,basicSalary);
prepareStatement.setString(7,panNumber);
prepareStatement.setString(8,aadharCardNumber);
prepareStatement.executeUpdate();
resultSet = prepareStatement.getGeneratedKeys();
resultSet.next();
int id = resultSet.getInt(1);
employeeDTO.setEmployeeId("A"+id);
resultSet.close();
prepareStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}



public List<EmployeeDTO> getAll() throws DAOException
{
List<EmployeeDTO> employees;
employees = new LinkedList<>();
try
{
Connection connection = DAOConnection.getConnection();
Statement statement;
statement = connection.createStatement();
ResultSet resultSet;
resultSet = statement.executeQuery("SELECT employee.id,employee.name,employee.designation_code,designation.title,employee.date_of_birth,employee.gender,employee.is_indian,employee.basic_salary,employee.pan_number,employee.aadhar_card_number FROM employee INNER JOIN designation ON employee.designation_code=designation.code ORDER BY employee.id");

int employeeId;
String name;
int designationCode;
String title;
java.sql.Date dateOfBirth; //cause we're receiving Date object from an sql database
String gender;
boolean isIndian;
BigDecimal basicSalary;
String panNumber;
String aadharCardNumber;
EmployeeDTO employeeDTO;
while(resultSet.next())
{
employeeId = resultSet.getInt("id");
name = resultSet.getString("name").trim();
designationCode = resultSet.getInt("designation_code");
title = resultSet.getString("title").trim();
dateOfBirth = resultSet.getDate("date_of_birth");
gender = resultSet.getString("gender");
isIndian = resultSet.getBoolean("is_indian");
basicSalary = resultSet.getBigDecimal("basic_salary");
panNumber = resultSet.getString("pan_number").trim();
aadharCardNumber = resultSet.getString("aadhar_card_number").trim();
employeeDTO = new EmployeeDTO();
employeeDTO.setEmployeeId("A" + employeeId);
employeeDTO.setName(name);
employeeDTO.setDesignationCode(designationCode);
employeeDTO.setTitle(title);
employeeDTO.setDateOfBirth(dateOfBirth);
employeeDTO.setGender(gender);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);
employees.add(employeeDTO);
}
resultSet.close();
statement.close();
connection.close();
return employees;
}catch(Exception e)
{
throw new DAOException(e.getMessage());
}
}
}