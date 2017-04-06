package mx.edu.utng.wsvaquero;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Axel on 05/04/2017.
 */
public class Vaquero implements KvmSerializable {

    private int id;
    private String name;
    private String last;
    private int age;
    private int type;
    private String nickname;
    private Float speed;

    public Vaquero(int id, String name, String last, int age, int type, String nick, Float speed) {

        this.id = id;
        this.name = name;
        this.last = last;
        this.age = age;
        this.type = type;
        this.nickname = nick;
        this.speed = speed;
    }
    public Vaquero(){
        this(0,"","",0,0,"",0.0F);
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return id;
            case 1:
                return name;
            case 2:
                return last;
            case 3:
                return age;
            case 4:
                return nickname;

            case 5:
                return type;
            case 6:
                return speed;
        }

        return  null;
    }

    @Override
    public int getPropertyCount()
    {
        return 7;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i){
            case 0:
                id =Integer.parseInt(o.toString());
                break;
            case 1:
                name = o.toString();
                break;
            case 2:
                last = o.toString();
                break;
            case 3:
                age = Integer.parseInt(o.toString());
                break;
            case 4:
                nickname = o.toString();
                break;
            case 5:
                type = Integer.parseInt(o.toString());

                break;
            case 6:
                speed = Float.parseFloat(o.toString());
                break;

        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i) {
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "id";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "name";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "last";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "age";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "nickname";

                break;
            case 5:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "type";
                break;
            case 6:
                propertyInfo.type = Float.class;
                propertyInfo.name = "speed";
                break;
            default:
                break;
        }

    }
}