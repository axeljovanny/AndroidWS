package mx.edu.utng.wsasentamiento;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

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
 * Created by axel_ on 04/04/2017.
 */

public class AsentamientoWS extends AppCompatActivity implements View.OnClickListener{
    private EditText etCodigoPostal;
    private EditText etConsecutivo;
    private EditText etCveEstado;
    private EditText etAsentamiento;
    private ToggleButton tbActivo;
    private EditText etMunicipio;
    private EditText etTipo;
    private EditText etCiudad;
    private Button btGuardar;
    private Button btListar;
    private Asentamiento asentamiento = null;

    final String NAMESPACE =
            "http://ws.utng.edu.mx";
    final SoapSerializationEnvelope envelope =
            new SoapSerializationEnvelope(SoapEnvelope.VER11);
    static String URL =
            "http://192.168.24.63:8080/Asentamiento/services/AsentamientoWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asentamiento);
        components();

    }

    private void components() {
        etCodigoPostal = (EditText) findViewById(R.id.et_codigo);
        etConsecutivo = (EditText) findViewById(R.id.et_consecutivo);
        etCveEstado = (EditText) findViewById(R.id.et_cve_estado);
        etAsentamiento = (EditText) findViewById(R.id.et_asentamiento);
        etCiudad = (EditText) findViewById(R.id.et_ciudad);
        etMunicipio = (EditText) findViewById(R.id.et_municipio);
        etTipo = (EditText) findViewById(R.id.et_tipo);

        btGuardar = (Button) findViewById(R.id.bt_save);
        btListar = (Button) findViewById(R.id.bt_list);
        tbActivo = (ToggleButton) findViewById(R.id.tb_activo);
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
        String codigoPostal = etCodigoPostal.getText().toString();
        String consecutivo = etCodigoPostal.getText().toString();
        String cveEstado = etCveEstado.getText().toString();
        String asentamiento = etAsentamiento.getText().toString();
        String activo = tbActivo.getText().toString();
        String municipio = etMunicipio.getText().toString();
        String tipo = etTipo.getText().toString();
        String ciudad = etCiudad.getText().toString();

        if (v.getId() == btGuardar.getId()) {
            if (codigoPostal != null && !codigoPostal.isEmpty() &&
                    consecutivo != null && !consecutivo.isEmpty() &&
                    cveEstado != null && !cveEstado.isEmpty() &&
                    asentamiento != null && !asentamiento.isEmpty() &&
                    activo != null && !activo.isEmpty() &&
                    municipio != null && !municipio.isEmpty() &&
                    tipo != null && !tipo.isEmpty() &&
                    ciudad != null && !ciudad.isEmpty()) {
                try {
                    if (getIntent().getExtras().getString("accion")
                            .equals("modificar")) {
                        UpdateAsentamiento tarea = new UpdateAsentamiento();
                        tarea.execute();
                    }

                } catch (Exception e) {
                    //Cuando no se haya mandado una accion por defecto es insertar.
                    InsertAsentamiento tarea = new InsertAsentamiento();
                    tarea.execute();
                }
            } else {
                Toast.makeText(this, "llenar todos los campos", Toast.LENGTH_LONG).show();
            }

        }
        if (btListar.getId() == v.getId()) {
            startActivity(new Intent(AsentamientoWS.this, ListAsentamiento.class));
        }
    }//fin conClick



    private class InsertAsentamiento extends
            AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            final String METHOD_NAME = "addAsentamiento";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request =
                    new SoapObject(NAMESPACE, METHOD_NAME);

            asentamiento = new Asentamiento();
            asentamiento.setProperty(0, 0);
            obtenerDatos();

            PropertyInfo info = new PropertyInfo();
            info.setName("asentamiento");
            info.setValue(asentamiento);
            info.setType(asentamiento.getClass());
            request.addProperty(info);
            envelope.setOutputSoapObject(request);
            envelope.addMapping(NAMESPACE, "asentamiento", Asentamiento.class);

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
               // cleanEditTex();
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
    private class UpdateAsentamiento extends
            AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            final String METHOD_NAME = "editAsentamiento";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            asentamiento = new Asentamiento();
            asentamiento.setProperty(0, getIntent().getExtras().getString("valor0"));

            obtenerDatos();

            PropertyInfo info = new PropertyInfo();
            info.setName("asentamiento");
            info.setValue(asentamiento);
            info.setType(asentamiento.getClass());

            request.addProperty(info);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.setOutputSoapObject(request);

            envelope.addMapping(NAMESPACE, "asentamiento", asentamiento.getClass());

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
                Toast.makeText(getApplicationContext(), "Actualizado",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error al actualizar",
                        Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void obtenerDatos() {
        asentamiento.setProperty(1, etCodigoPostal.getText().toString());
        asentamiento.setProperty(2, Integer.parseInt(etConsecutivo.getText().toString()));
        asentamiento.setProperty(3, Integer.parseInt(etCveEstado.getText().toString()));
        asentamiento.setProperty(4, etAsentamiento.getText().toString());


        if(tbActivo.isChecked()){
            asentamiento.setProperty(5,"1");
        }else{
            asentamiento.setProperty(5,"0");
        }
        asentamiento.setProperty(6, etMunicipio.getText().toString());

        asentamiento.setProperty(7, etTipo.getText().toString());
        asentamiento.setProperty(8, Integer.parseInt(etCiudad.getText().toString()));

    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle datosRegreso = this.getIntent().getExtras();
        try {
            Log.i("Dato", datosRegreso.getString("valor5"));

            etCodigoPostal.setText(datosRegreso.getString("valor1"));
            etConsecutivo.setText(datosRegreso.getString("valor2"));
            etCveEstado.setText(datosRegreso.getString("valor3"));
            etAsentamiento.setText(datosRegreso.getString("valor4"));
            if (datosRegreso.getString("valor5").equals("1")) {
                tbActivo.setChecked(true);
            } else {
                tbActivo.setChecked(false);
            }

            etMunicipio.setText(datosRegreso.getString("valor6"));
            etTipo.setText(datosRegreso.getString("valor7"));
            etCiudad.setText(datosRegreso.getString("valor8"));
        } catch (Exception e) {
            Log.e("Error al cargar", e.toString());
        }

    }

}
