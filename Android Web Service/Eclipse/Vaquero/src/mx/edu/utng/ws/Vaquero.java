package mx.edu.utng.ws;

public class Vaquero {
	private int id;
	private String name;
	private String last;
	private int age;
	private String nickname;
	private int type;
	private float speed;
	
	public Vaquero(int id, String name, String last, int age, String nickname, int type, float speed) {
		super();
		this.id = id;
		this.name = name;
		this.last = last;
		this.age = age;
		this.nickname = nickname;
		this.type = type;
		this.speed = speed;
	}
	public  Vaquero() {
		this(0, "", "", 0, "", 0, 0.0f);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	@Override
	public String toString() {
		return "Vaquero [id=" + id + ", name=" + name + ", last=" + last + ", age=" + age + ", nickname=" + nickname
				+ ", type=" + type + ", speed=" + speed + "]";
	}
	 
	
	
	
		
	}


