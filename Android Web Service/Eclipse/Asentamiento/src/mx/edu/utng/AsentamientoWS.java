package mx.edu.utng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AsentamientoWS {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private PreparedStatement ps;
	
	
	public AsentamientoWS() {
		conectar();
	}
	private void conectar(){
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WSTest",
					"postgres","jnt");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addAsentamiento(Asentamiento asentamiento){
		int result =0;
		if (connection != null) {
			try {
				ps = connection.prepareStatement(
						"INSERT INTO asentamiento (codigo_postal, consecutivo, cve_estado, asentamiento, activo, municipio, tipo, ciudad) "
						+ "VALUES (?,?,?,?,?,?,?,?);");
				ps.setString(1, asentamiento.getCodigoPostal());
				ps.setInt(2, asentamiento.getConsecutivo());
				ps.setInt(3,asentamiento.getCveEstado());
				ps.setString(4, asentamiento.getAsentamiento());
				ps.setInt(5, asentamiento.getActivo());
				ps.setString(6, asentamiento.getMunicipio());
				ps.setString(7, asentamiento.getTipo());
				ps.setInt(8, asentamiento.getCiudad());

				result = ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int editAsentamiento(Asentamiento asentamiento){
		int result =0;
		if (connection != null) {
			try { 
				ps = connection.prepareStatement(
						"UPDATE asentamiento SET codigo_postal =? , "
						+ "consecutivo = ? ,"
						+ "cve_estado = ? , "
						+ "asentamiento = ? , "
						+ "activo = ? , "
						+ "municipio = ? , "
						+ "tipo = ? , "
						+ "ciudad = ? "
						+ "WHERE id = ?;");
				ps.setString(1, asentamiento.getCodigoPostal());
				ps.setInt(2, asentamiento.getConsecutivo());
				ps.setInt(3,asentamiento.getCveEstado());
				ps.setString(4, asentamiento.getAsentamiento());
				ps.setInt(5, asentamiento.getActivo());
				ps.setString(6, asentamiento.getMunicipio());
				ps.setString(7, asentamiento.getTipo());
				ps.setInt(8, asentamiento.getCiudad());
				ps.setInt(9, asentamiento.getId());
				result = ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public int removeAsentamiento(int id){
		int result =0;
		if (connection != null) {
			try {
				ps = connection.prepareStatement(
						"DELETE FROM asentamiento WHERE id = ?;");
				ps.setInt(1, id);
				result =ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Asentamiento[] getAsentamientos(){
		Asentamiento [] result = null;
		List<Asentamiento> asentamientos = new ArrayList<Asentamiento>();
		
		if (connection != null) {
			try {
				statement = connection.createStatement();
				resultSet = statement.executeQuery(
						"SELECT * FROM asentamiento;");
				while (resultSet.next()) {
					Asentamiento asentamiento = new Asentamiento(
							resultSet.getInt("id"),
							resultSet.getString("codigo_postal"),
							resultSet.getInt("consecutivo"),
							resultSet.getInt("cve_estado"),
							resultSet.getString("asentamiento"), 
							resultSet.getInt("activo"),
							resultSet.getString("municipio"),
							resultSet.getString("tipo"),
							resultSet.getInt("ciudad"));
					asentamientos.add(asentamiento);
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return asentamientos.toArray(new Asentamiento[asentamientos.size()]);
	}
	public Asentamiento getOneAsentamiento(int id){
		Asentamiento asentamiento = null;
		
		if (connection != null) {
			try {
				ps = connection.prepareStatement("SELECT * FROM asentamiento WHERE id = ?;");
				ps.setInt(1, id);
			    resultSet = ps.executeQuery();
				if(resultSet.next()) {
					asentamiento = new Asentamiento(
							resultSet.getInt("id"),
							resultSet.getString("codigo_postal"),
							resultSet.getInt("consecutivo"),
							resultSet.getInt("cve_estado"),
							resultSet.getString("asentamiento"), 
							resultSet.getInt("activo"),
							resultSet.getString("municipio"),
							resultSet.getString("tipo"),
							resultSet.getInt("ciudad"));
					
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return asentamiento;
	}
}


