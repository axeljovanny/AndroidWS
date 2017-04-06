package mx.edu.utng.ws;

public class Actividad {
	private int id;
	private String descripcion;
	private String fechaInicio;
	private String fechaFin;
	private int totalHoras;
	private String personal;
	
	public Actividad(int id, String descripcion, String fechaInicio, String fechaFin, int totalHoras,
			String personal) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.totalHoras = totalHoras;
		this.personal = personal;
	}
	
	public Actividad(){
	this(0,"","","",0,"");
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public int getTotalHoras() {
		return totalHoras;
	}
	public void setTotalHoras(int totalHoras) {
		this.totalHoras = totalHoras;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}

	@Override
	public String toString() {
		return "Actividad [id=" + id + ", descripcion=" + descripcion + ", fechaInicio="
				+ fechaInicio + ", fechaFin=" + fechaFin + ", totalHoras=" + totalHoras + ", personal=" + personal
				+ "]";
	}
	
	
	


}
