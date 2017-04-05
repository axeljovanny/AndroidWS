package mx.edu.utng;


public class Asentamiento {

	private int id;
	private String codigoPostal;
	private int consecutivo;
	private int cveEstado;
	private String asentamiento;
	private int activo;
	private String municipio;
	private String tipo;
	private int ciudad;
	
	public Asentamiento(int id, String codigoPostal, int consecutivo, int cveEstado, String asentamiento,
			int activo, String municipio, String tipo, int ciudad) {
		super();
		this.id = id;
		this.codigoPostal = codigoPostal;
		this.consecutivo = consecutivo;
		this.cveEstado = cveEstado;
		this.asentamiento = asentamiento;
		this.activo = activo;
		this.municipio = municipio;
		this.tipo = tipo;
		this.ciudad = ciudad;
	}
	
	public Asentamiento(){
		this(0, "", 0, 0, "", 0, "", "", 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public int getCveEstado() {
		return cveEstado;
	}

	public void setCveEstado(int cveEstado) {
		this.cveEstado = cveEstado;
	}

	public String getAsentamiento() {
		return asentamiento;
	}

	public void setAsentamiento(String asentamiento) {
		this.asentamiento = asentamiento;
	}

	

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getCiudad() {
		return ciudad;
	}

	public void setCiudad(int ciudad) {
		this.ciudad = ciudad;
	}

	@Override
	public String toString() {
		return "Asentamiento [id=" + id + ", codigoPostal=" + codigoPostal + ", consecutivo=" + consecutivo
				+ ", cveEstado=" + cveEstado + ", asentamiento=" + asentamiento + ", activo=" + activo + ", municipio="
				+ municipio + ", tipo=" + tipo + ", ciudad=" + ciudad + "]";
	}
	
	
}
