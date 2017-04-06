package mx.edu.utng.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ActividadWS {
	
		private Connection connection;
		private Statement statement;
		private ResultSet resultSet;
		private PreparedStatement ps;
		
		
		public ActividadWS() {
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
		
		public int addActividad(Actividad actividad){
			int result =0;
			if (connection != null) {
				try {
					ps = connection.prepareStatement(
							"INSERT INTO actividad_impartida (descripcion,fecha_inicio,fecha_fin,total_horas,personal) "
							+ "VALUES (?,?,?,?,?);");
					ps.setString(1, actividad.getDescripcion());
					ps.setString(2, actividad.getFechaInicio());
					ps.setString(3,actividad.getFechaFin());
					ps.setInt(4, actividad.getTotalHoras());
					ps.setString(5, actividad.getPersonal());
					
					
					result = ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		
		public int editActividad(Actividad actividad){
			int result =0;
			if (connection != null) {
				try {
					ps = connection.prepareStatement(
							"UPDATE actividad_impartida SET descripcion =? , " 
							+ "fecha_inicio = ? ,"
							+ "fecha_fin = ? , "
							+ "total_horas = ? ,"
	 						+ "personal = ? "
							+ "WHERE id = ?;");
					ps.setString(1, actividad.getDescripcion());
					ps.setString(2, actividad.getFechaInicio());
					ps.setString(3,actividad.getFechaFin());
					ps.setInt(4, actividad.getTotalHoras());
					ps.setString(5, actividad.getPersonal());
					ps.setInt(6, actividad.getId());
					result = ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		public int removeActividad(int id){
			int result =0;
			if (connection != null) {
				try {
					ps = connection.prepareStatement(
							"DELETE FROM actividad_impartida WHERE id = ?;");
					ps.setInt(1, id);
					result =ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		
		public Actividad[] getActividades(){
			Actividad [] result = null;
			List<Actividad> actividades = new ArrayList<Actividad>();
			
			if (connection != null) {
				try {
					statement = connection.createStatement();
					resultSet = statement.executeQuery(
							"SELECT * FROM actividad_impartida;");
					while (resultSet.next()) {
						Actividad actividad = new Actividad(
								resultSet.getInt("id"),
								resultSet.getString("descripcion"),
								resultSet.getString("fecha_inicio"),
								resultSet.getString("fecha_fin"),
								resultSet.getInt("total_horas"),
								resultSet.getString("personal"));
						actividades.add(actividad);
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return actividades.toArray(new Actividad[actividades.size()]);
		}
		public Actividad getOneActividad(int id){
			Actividad actividad = null;
			
			if (connection != null) {
				try {
					ps = connection.prepareStatement("SELECT * FROM actividad_impartida WHERE id = ?;");
					ps.setInt(1, id);
				    resultSet = ps.executeQuery();
					if(resultSet.next()) {
						actividad = new Actividad(
								resultSet.getInt("id"),
								resultSet.getString("descripcion"),
								resultSet.getString("fecha_inicio"),
								resultSet.getString("fecha_fin"),
								resultSet.getInt("total_horas"),
								resultSet.getString("personal"));
						
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return actividad;
		}
	}


