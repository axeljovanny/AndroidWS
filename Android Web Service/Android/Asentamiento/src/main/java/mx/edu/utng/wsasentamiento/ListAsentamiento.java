package mx.edu.utng.wsasentamiento;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by axel_ on 04/04/2017.
 */

public class ListAsentamiento extends ListActivity {


    final String NAMESPACE = "http://ws.utng.edu.mx";

    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
            SoapEnvelope.VER11);

    private ArrayList<Asentamiento> asentamientos = new ArrayList<Asentamiento>();
    private int idSeleccionado;
    private int posicionSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Asentamientos query = new Asentamientos();
        query.execute();
        registerForContextMenu(getListView());


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_modificar:
                Asentamiento asentamiento = asentamientos.get(posicionSeleccionado);
                Bundle bundleAsentamiento = new Bundle();
                for (int i = 0; i < asentamiento.getPropertyCount(); i++) {
                    bundleAsentamiento.putString("valor" + i, asentamiento.getProperty(i)
                            .toString());
                }
                bundleAsentamiento.putString("accion", "modificar");
                Intent intent = new Intent(ListAsentamiento.this, AsentamientoWS.class);
                intent.putExtras(bundleAsentamiento);
                startActivity(intent);

                return true;
            case R.id.item_eliminar:
                DeleteAsentamiento eliminar = new DeleteAsentamiento();
                eliminar.execute();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getApplicationContext());
        menuInflater.inflate(R.menu.menu_back, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_regresar:
                startActivity(new Intent(ListAsentamiento.this, AsentamientoWS.class));
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(getListView().getAdapter().getItem(info.position).toString());
        idSeleccionado = (Integer) asentamientos.get(info.position).getProperty(0);
        posicionSeleccionado = info.position;
        inflater.inflate(R.menu.menu_options, menu);
    }




    private class Asentamientos extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;
            final String METHOD_NAME = "getAsentamientos";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
            asentamientos.clear();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(AsentamientoWS.URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();
                if (response != null) {
                    for (SoapObject objSoap : response) {
                        Asentamiento asentamiento = new Asentamiento();
                        asentamiento.setProperty(0, Integer.parseInt(objSoap.getProperty("id").toString()));
                        asentamiento.setProperty(1, objSoap.getProperty("codigoPostal").toString());
                        asentamiento.setProperty(2, Integer.parseInt(objSoap.getProperty("consecutivo").toString()));
                        asentamiento.setProperty(3, Integer.parseInt(objSoap.getProperty("cveEstado").toString()));
                        asentamiento.setProperty(4, objSoap.getProperty("asentamiento").toString());
                        asentamiento.setProperty(5, objSoap.getProperty("activo").toString());
                        asentamiento.setProperty(6, objSoap.getProperty("municipio").toString());
                        asentamiento.setProperty(7, objSoap.getProperty("tipo").toString());
                        asentamiento.setProperty(8, Integer.parseInt(objSoap.getProperty("ciudad").toString()));

                        asentamientos.add(asentamiento);
                    }
                }

            } catch (XmlPullParserException e) {
                Log.e("Error XMLPullParser", e.toString());
                result = false;
            } catch (HttpResponseException e) {
                Log.e("Error HTTP", e.toString());

                result = false;
            } catch (IOException e) {
                Log.e("Error IO", e.toString());
                result = false;
            } catch (ClassCastException e) {
                try {
                    SoapObject objSoap = (SoapObject) envelope.getResponse();
                    Asentamiento asentamiento = new Asentamiento();
                    asentamiento.setProperty(0, Integer.parseInt(objSoap.getProperty("id").toString()));
                    asentamiento.setProperty(1, objSoap.getProperty("codigoPostal").toString());
                    asentamiento.setProperty(2, Integer.parseInt(objSoap.getProperty("consecutivo").toString()));
                    asentamiento.setProperty(3, Integer.parseInt(objSoap.getProperty("cveEstado").toString()));
                    asentamiento.setProperty(4, objSoap.getProperty("asentamiento").toString());
                    asentamiento.setProperty(5, Integer.parseInt(objSoap.getProperty("activo").toString()));
                    asentamiento.setProperty(6, objSoap.getProperty("municipio").toString());
                    asentamiento.setProperty(7, objSoap.getProperty("tipo").toString());
                    asentamiento.setProperty(8, Integer.parseInt(objSoap.getProperty("ciudad").toString()));
                    asentamientos.add(asentamiento);
                } catch (SoapFault e1) {
                    Log.e("Error SoapFault", e.toString());
                    result = false;
                }
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                final String[] datos = new String[asentamientos.size()];
                for (int i = 0; i < asentamientos.size(); i++) {
                    datos[i] = asentamientos.get(i).getProperty(0) + " - "
                            + asentamientos.get(i).getProperty(1);

                }

                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                        ListAsentamiento.this,
                        android.R.layout.simple_list_item_1, datos);
                setListAdapter(adaptador);
            } else {
                Toast.makeText(getApplicationContext(), "No se encontraron datos.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }









    private class DeleteAsentamiento extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            final String METHOD_NAME = "removeAsentamiento";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("id", idSeleccionado);

            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(AsentamientoWS.URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                String res = resultado_xml.toString();

                if (!res.equals("0")) {
                    result = true;
                }

            } catch (Exception e) {
                Log.e("Error", e.toString());
                result = false;
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getApplicationContext(),
                        "Eliminado", Toast.LENGTH_SHORT).show();
                Asentamientos consulta = new Asentamientos();
                consulta.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Error al eliminar",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}


