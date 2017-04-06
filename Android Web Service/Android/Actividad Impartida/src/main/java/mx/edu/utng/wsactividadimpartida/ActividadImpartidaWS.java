package mx.edu.utng.wsactividadimpartida;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by axel on 05/04/2017.
 */
public class ActividadImpartidaWS extends AppCompatActivity implements View.OnClickListener {
    private EditText etDescripcion;
    private EditText etFechaInicio;
    private EditText etFechaFin;
    private EditText etTotalHoras;
    private EditText etPersonal;

    private Button btGuardar;
    private Button btListar;
    private ActividadImpartida actividadImpartida = null;


    final String NAMESPACE =
            "http://ws.utng.edu.mx";
    final SoapSerializationEnvelope envelope =
            new SoapSerializationEnvelope(SoapEnvelope.VER11);
    static String URL =
            "http://192.168.1.68:8080/WSActividadImpartida/services/ActividadWS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_impartida_ws);
        components();

    }

    private void components() {
        etDescripcion = (EditText) findViewById(R.id.et_descripcion);
        etFechaInicio = (EditText) findViewById(R.id.et_fecha_inicio);
        etFechaFin = (EditText) findViewById(R.id.et_fecha_fin);
        etTotalHoras = (EditText) findViewById(R.id.et_total_horas);
        etPersonal = (EditText) findViewById(R.id.et_personal);

        btGuardar = (Button) findViewById(R.id.bt_save);
        btListar = (Button) findViewById(R.id.bt_list);
        btGuardar.setOnClickListener(this);
        btListar.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_consume_w, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        String descripcion = etDescripcion.getText().toString();
        String inicio = etFechaInicio.getText().toString();
        String fin = etFechaFin.getText().toString();
        String horas = etTotalHoras.getText().toString();
        String personal = etPersonal.getText().toString();


        if (v.getId() == btGuardar.getId()) {
            if (descripcion != null && !descripcion.isEmpty() &&
                    inicio != null && !inicio.isEmpty() &&
                    fin != null && !fin.isEmpty() &&
                    horas != null && !horas.isEmpty() &&
                    personal != null && !personal.isEmpty()) {
                try {
                    if (getIntent().getExtras().getString("accion")
                            .equals("modificar")) {
                        UpdateActividadImpartida tarea = new UpdateActividadImpartida();
                        tarea.execute();

                    }

                } catch (Exception e) {
                    //Cuando no se haya mandado una accion por defecto es insertar.
                    InsertActividadImpartida tarea = new InsertActividadImpartida();
                    tarea.execute();
                }
            } else {
                Toast.makeText(this, "llenar todos los campos", Toast.LENGTH_LONG).show();
            }

        }
        if (btListar.getId() == v.getId()) {
            startActivity(new Intent(ActividadImpartidaWS.this, ListaActividadImpartida.class));
        }
    }//fin conClick



    private class InsertActividadImpartida extends
            AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "addActividad";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request =
                    new SoapObject(NAMESPACE, METHOD_NAME);

            actividadImpartida = new ActividadImpartida();
            actividadImpartida.setProperty(0, 0);
            obtenerDatos();

            PropertyInfo info = new PropertyInfo();
            info.setName("actividadImpartida");
            info.setValue(actividadImpartida);
            info.setType(actividadImpartida.getClass());
            request.addProperty(info);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "actividadImpartida", ActividadImpartida.class);

            /* Para serializar flotantes y otros tipos no cadenas o enteros*/
            MarshalFloat mf = new MarshalFloat();
            mf.register(envelope);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive response =
                        (SoapPrimitive) envelope.getResponse();
                String res = response.toString();
                if (!res.equals("1")) {
                    result = false;
                }

            } catch (Exception e) {
                Log.e("Error ", e.getMessage());
                result = false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(),
                        "Registro exitoso.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error al insertar.",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }//fin tarea insertar

    private class UpdateActividadImpartida extends
            AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            final String METHOD_NAME = "editActividad";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            actividadImpartida = new ActividadImpartida();
            actividadImpartida.setProperty(0, getIntent().getExtras().getString("valor0"));

            obtenerDatos();

            PropertyInfo info = new PropertyInfo();
            info.setName("actividadImpartida");
            info.setValue(actividadImpartida);
            info.setType(actividadImpartida.getClass());

            request.addProperty(info);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);

            envelope.addMapping(NAMESPACE, "actividadImpartida", actividadImpartida.getClass());

            MarshalFloat mf = new MarshalFloat();
            mf.register(envelope);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                String res = resultado_xml.toString();

                if (!res.equals("1")) {
                    result = false;
                }

            } catch (HttpResponseException e) {
                Log.e("Error HTTP", e.toString());
            } catch (IOException e) {
                Log.e("Error IO", e.toString());
            } catch (XmlPullParserException e) {
                Log.e("Error XmlPullParser", e.toString());
            }

            return result;

        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getApplicationContext(), "Actualizado OK",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error al actualizar",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void obtenerDatos() {
        actividadImpartida.setProperty(1, etDescripcion.getText().toString());
        actividadImpartida.setProperty(2, etFechaInicio.getText().toString());
        actividadImpartida.setProperty(3, etFechaFin.getText().toString());
        actividadImpartida.setProperty(4, etTotalHoras.getText().toString());
        actividadImpartida.setProperty(5, etPersonal.getText().toString());



    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle datosRegreso = this.getIntent().getExtras();
        try {

            etDescripcion.setText(datosRegreso.getString("valor1"));
            etFechaInicio.setText(datosRegreso.getString("valor2"));
            etFechaFin.setText(datosRegreso.getString("valor3"));
            etTotalHoras.setText(datosRegreso.getString("valor4"));
            etPersonal.setText(datosRegreso.getString("valor5"));


        } catch (Exception e) {
            Log.e("Error al Recargar", e.toString());

        }

    }
}

