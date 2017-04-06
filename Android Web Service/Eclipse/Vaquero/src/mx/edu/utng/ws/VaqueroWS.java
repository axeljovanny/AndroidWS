package mx.edu.utng.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VaqueroWS{
private Connection connection;
private Statement statement;
private ResultSet resultSet;
private PreparedStatement ps;


public VaqueroWS() {
	conectar();
}
private void conectar(){
	try {
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WSTest",
				"postgres","admin");
		
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}catch (SQLException e) {
		e.printStackTrace();
	}
}

public int addVaquero(Vaquero vaquero){
	int result =0;
	if (connection != null) {
		try {
			ps = connection.prepareStatement(
					"INSERT INTO vaquero (name,last,age,nickname,type,speed) "
					+ "VALUES (?,?,?,?,?,?);");
			ps.setString(1, vaquero.getName());
			ps.setString(2, vaquero.getLast());
			ps.setInt(3,vaquero.getAge());
			ps.setString(4, vaquero.getNickname());
			ps.setInt(5, vaquero.getType());
			ps.setFloat(6, vaquero.getSpeed());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return result;
}

public int editVaquero(Vaquero vaquero){
	int result =0;
	if (connection != null) {
		try {
			ps = connection.prepareStatement(
					"UPDATE vaquero SET name =? , "
					+ "last = ? ,"
					+ "age = ? , "
					+ "nickname = ?, "
					+ "type = ?, "
					+ "speed = ? "
					+ "WHERE id = ?;");
			ps.setString(1, vaquero.getName());
			ps.setString(2, vaquero.getLast());
			ps.setInt(3,vaquero.getAge());
			ps.setString(4, vaquero.getNickname());
			ps.setInt(5, vaquero.getType());
			ps.setFloat(6, vaquero.getSpeed());
			ps.setInt(7, vaquero.getId());

			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return result;
}
public int removeVaquero(int id){
	int result =0;
	if (connection != null) {
		try {
			ps = connection.prepareStatement(
					"DELETE FROM vaquero WHERE id = ?;");
			ps.setInt(1, id);
			result =ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return result;
}

public Vaquero[] getVaqueros(){
	Vaquero [] result = null;
	List<Vaquero> vaqueros = new ArrayList<Vaquero>();
	
	if (connection != null) {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT * FROM vaquero;");
			while (resultSet.next()) {
				Vaquero vaquero = new Vaquero(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("last"),
						resultSet.getInt("age"),
						resultSet.getString("nickname"),
						resultSet.getInt("type"),
						resultSet.getFloat("speed")
						);
				vaqueros.add(vaquero);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return vaqueros.toArray(new Vaquero[vaqueros.size()]);
}
public Vaquero getOneVaquero(int id){
	Vaquero vaquero = null;
	
	if (connection != null) {
		try {
			ps = connection.prepareStatement("SELECT * FROM vaquero WHERE id = ?;");
			ps.setInt(1, id);
		    resultSet = ps.executeQuery();
			if(resultSet.next()) {
				vaquero = new Vaquero(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getString("last"),
						resultSet.getInt("age"),
						resultSet.getString("nickname"),
						resultSet.getInt("type"),
						resultSet.getFloat("speed")
						);
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return vaquero;
}
}
