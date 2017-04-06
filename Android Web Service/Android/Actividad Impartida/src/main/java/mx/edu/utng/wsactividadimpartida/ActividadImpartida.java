package mx.edu.utng.wsactividadimpartida;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Axel on 05/04/2017.
 */
public class ActividadImpartida implements KvmSerializable {

    private int id;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private int totalHoras;
    private String personal;

    public ActividadImpartida(int id, String descripcion, String fechaInicio, String fechaFin, int totalHoras, String personal) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalHoras = totalHoras;
        this.personal = personal;
    }
    public ActividadImpartida(){
        this(0,"","","",0,"");
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return id;
            case 1:
                return descripcion;
            case 2:
                return fechaInicio;
            case 3:
                return fechaFin;
            case 4:
                return totalHoras;

            case 5:
                return personal;

        }

        return  null;
    }

    @Override
    public int getPropertyCount() {
        return 6;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i){
            case 0:
                id =Integer.parseInt(o.toString());
                break;
            case 1:
                descripcion = o.toString();
                break;
            case 2:
                fechaInicio = o.toString();
                break;
            case 3:
                fechaFin = o.toString();
                break;
            case 4:
                totalHoras = Integer.parseInt(o.toString());
                break;
            case 5:
                personal = o.toString();

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
                propertyInfo.name = "descripcion";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "fechaInicio";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "fechaFin";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "totalHoras";

                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "personal";
                break;

            default:
                break;
        }
    }
}

