package mx.edu.utng.wsvaquero;

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
 * Created by Axel on 05/04/2017.
 */
public class VaqueroWS extends AppCompatActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etLast;
    private EditText etAge;
    private EditText etType;
    private EditText etNick;
    private EditText etSpeed;
    private Button btGuardar;
    private Button btListar;
    private Vaquero vaquero = null;


    final String NAMESPACE =
            "http://ws.utng.edu.mx";
    final SoapSerializationEnvelope envelope =
            new SoapSerializationEnvelope(SoapEnvelope.VER11);
    static String URL =
            "http://192.168.1.68:8080/WSVaquero/services/VaqueroWS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaquero_ws);
        components();

    }

    private void components() {
        etName = (EditText) findViewById(R.id.et_name);
        etAge = (EditText) findViewById(R.id.et_age);
        etLast = (EditText) findViewById(R.id.et_last);
        etType = (EditText) findViewById(R.id.et_type);
        etNick = (EditText) findViewById(R.id.et_nick_name);
        etSpeed = (EditText) findViewById(R.id.et_speed);
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
        String name = etName.getText().toString();
        String age = etAge.getText().toString();
        String last = etLast.getText().toString();
        String type = etType.getText().toString();
        String nick = etNick.getText().toString();
        String speed = etSpeed.getText().toString();

        if (v.getId() == btGuardar.getId()) {
            if (name != null && !name.isEmpty() &&
                    age != null && !age.isEmpty() &&
                    last != null && !last.isEmpty() &&
                    type != null && !type.isEmpty() &&
                    nick != null && !nick.isEmpty() &&
                    speed != null && !speed.isEmpty()) {
                try {
                    if (getIntent().getExtras().getString("accion")
                            .equals("modificar")) {
                        UpdateVaquero tarea = new UpdateVaquero();
                        tarea.execute();

                    }

                } catch (Exception e) {
                    //Cuando no se haya mandado una accion por defecto es insertar.
                    InsertVaquero tarea = new InsertVaquero();
                    tarea.execute();
                }
            } else {
                Toast.makeText(this, "llenar todos los campos", Toast.LENGTH_LONG).show();
            }

        }
        if (btListar.getId() == v.getId()) {
            startActivity(new Intent(VaqueroWS.this, ListVaquero.class));
        }
    }//fin conClick



    private class InsertVaquero extends
            AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "addVaquero";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request =
                    new SoapObject(NAMESPACE, METHOD_NAME);

            vaquero = new Vaquero();
            vaquero.setProperty(0, 0);
            obtenerDatos();

            PropertyInfo info = new PropertyInfo();
            info.setName("vaquero");
            info.setValue(vaquero);
            info.setType(vaquero.getClass());
            request.addProperty(info);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "vaquero", Vaquero.class);

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

    private class UpdateVaquero extends
            AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            final String METHOD_NAME = "editVaquero";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            vaquero = new Vaquero();
            vaquero.setProperty(0, getIntent().getExtras().getString("valor0"));

            obtenerDatos();

            PropertyInfo info = new PropertyInfo();
            info.setName("vaquero");
            info.setValue(vaquero);
            info.setType(vaquero.getClass());

            request.addProperty(info);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);

            envelope.addMapping(NAMESPACE, "vaquero", vaquero.getClass());

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
        vaquero.setProperty(1, etName.getText().toString());
        vaquero.setProperty(2, etLast.getText().toString());
        vaquero.setProperty(3, etAge.getText().toString());
        vaquero.setProperty(4, etNick.getText().toString());
        vaquero.setProperty(5, etType.getText().toString());

        vaquero.setProperty(6, etSpeed.getText().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle datosRegreso = this.getIntent().getExtras();
        try {

            etName.setText(datosRegreso.getString("valor1"));
            etLast.setText(datosRegreso.getString("valor2"));
            etAge.setText(datosRegreso.getString("valor3"));
            etNick.setText(datosRegreso.getString("valor4"));
            etType.setText(datosRegreso.getString("valor5"));

            etSpeed.setText(datosRegreso.getString("valor6"));
        } catch (Exception e) {
            Log.e("Error al Recargar", e.toString());

        }

    }
}

