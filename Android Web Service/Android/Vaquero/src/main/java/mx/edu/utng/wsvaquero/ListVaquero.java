package mx.edu.utng.wsvaquero;

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
 * Created by Axel on 05/04/2017.
 */
public class ListVaquero extends ListActivity {


    final String NAMESPACE = "http://ws.utng.edu.mx";

    final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
            SoapEnvelope.VER11);

    private ArrayList<Vaquero> vaqueros = new ArrayList<Vaquero>();
    private int idSeleccionado;
    private int posicionSeleccionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vaqueros query = new Vaqueros();
        query.execute();
        registerForContextMenu(getListView());


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_modificar:
                Vaquero vaquero = vaqueros.get(posicionSeleccionado);
                Bundle bundleVaquero = new Bundle();
                for (int i = 0; i < vaquero.getPropertyCount(); i++) {
                    bundleVaquero.putString("valor" + i, vaquero.getProperty(i)
                            .toString());
                }
                bundleVaquero.putString("accion", "modificar");
                Intent intent = new Intent(ListVaquero.this, VaqueroWS.class);
                intent.putExtras(bundleVaquero);
                startActivity(intent);

                return true;
            case R.id.item_eliminar:
                DeleteVaquero eliminar = new DeleteVaquero();
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
                startActivity(new Intent(ListVaquero.this, VaqueroWS.class));
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
        idSeleccionado = (Integer) vaqueros.get(info.position).getProperty(0);
        posicionSeleccionado = info.position;
        inflater.inflate(R.menu.menu_options, menu);
    }




    private class Vaqueros extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;
            final String METHOD_NAME = "getVaqueros";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
            vaqueros.clear();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(VaqueroWS.URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();
                if (response != null) {
                    for (SoapObject objSoap : response) {
                        Vaquero vaquero = new Vaquero();
                        vaquero.setProperty(0, Integer.parseInt(objSoap.getProperty("id").toString()));
                        vaquero.setProperty(1, objSoap.getProperty("name").toString());
                        vaquero.setProperty(2, objSoap.getProperty("last").toString());
                        vaquero.setProperty(3, Integer.parseInt(objSoap.getProperty("age").toString()));
                        vaquero.setProperty(4, objSoap.getProperty("nickname").toString());
                        vaquero.setProperty(5, Integer.parseInt(objSoap.getProperty("type").toString()));

                        vaquero.setProperty(6, Float.parseFloat(objSoap.getProperty("speed").toString()));
                        vaqueros.add(vaquero);
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
                    Vaquero vaquero = new Vaquero();
                    vaquero.setProperty(0, Integer.parseInt(objSoap.getProperty("id").toString()));
                    vaquero.setProperty(1, objSoap.getProperty("name").toString());
                    vaquero.setProperty(2, objSoap.getProperty("last").toString());
                    vaquero.setProperty(3, Integer.parseInt(objSoap.getProperty("age").toString()));
                    vaquero.setProperty(4, objSoap.getProperty("nickname").toString());
                    vaquero.setProperty(5, Integer.parseInt(objSoap.getProperty("type").toString()));

                    vaquero.setProperty(6, Float.parseFloat(objSoap.getProperty("speed").toString()));
                    vaqueros.add(vaquero);
                } catch (SoapFault e1) {
                    Log.e("Error SoapFault", e.toString());
                    result = false;
                }
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                final String[] datos = new String[vaqueros.size()];
                for (int i = 0; i < vaqueros.size(); i++) {
                    datos[i] = vaqueros.get(i).getProperty(0) + " - "
                            + vaqueros.get(i).getProperty(1)+ " - "
                            + vaqueros.get(i).getProperty(2)+ " - "
                            + vaqueros.get(i).getProperty(4)+ " - "
                            + vaqueros.get(i).getProperty(5)+ " - "
                            + vaqueros.get(i).getProperty(6);
                }

                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                        ListVaquero.this,
                        android.R.layout.simple_list_item_1, datos);
                setListAdapter(adaptador);
            } else {
                Toast.makeText(getApplicationContext(), "No se encontraron datos.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }









    private class DeleteVaquero extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            final String METHOD_NAME = "removeVaquero";
            final String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("id", idSeleccionado);

            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(VaqueroWS.URL);
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
                Vaqueros consulta = new Vaqueros();
                consulta.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Error al eliminar",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}


