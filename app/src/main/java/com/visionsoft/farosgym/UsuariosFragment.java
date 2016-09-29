package com.visionsoft.farosgym;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends ListFragment implements OnItemClickListener  {

    //private List<usuarios> mDataSourceList = new ArrayList<usuarios>();
    ConnectionClass conexion = new ConnectionClass();
    SimpleAdapter ADAhere;
    private  ProgressDialog progressBar;

    public UsuariosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        new GetUsers().execute();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        //para hacerlo con la clase usuarios
        //mDataSourceList = new usuarios().execute().get();
        //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mDataSourceList);
        //setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    //clase para controlar el loguin
    public class GetUsers extends AsyncTask<Void,Void,Void>
    {
        List<Map<String, String>> data = null;

        String mensaje = "";

        @Override
        protected void onPreExecute() {
            data = new ArrayList<Map<String, String>>();

            progressBar = new ProgressDialog(getContext());
            progressBar.setCancelable(false);
            progressBar.setMessage("Cargando...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }

        @Override
        protected void onPostExecute(Void r) {
            // create the grid item mapping

            String[] from = new String[] {"usuario", "UserId", "imagen"};

            int[] to = new int[] { R.id.title, R.id.subTitle, R.id.icon };
            ADAhere = new ExtendedSimpleAdapter(getActivity(), data, R.layout.drawe_item, from, to);
            setListAdapter(ADAhere);
            progressBar.dismiss();
        }

        public Bitmap decode (String encoded_img)
        {
            byte[] decodeString = Base64.decode(encoded_img, Base64.DEFAULT);
            Bitmap decodebitmap = BitmapFactory.decodeByteArray(
                    decodeString, 0, decodeString.length);
            return decodebitmap;
        }

        @Override
        protected Void doInBackground(Void... params) {
            //obtenemos los usuarios
            Connection conn = null;
            try {
                //realizamos la conexión
                conn = conexion.CONN();
                String query = "select * from Usertbl";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                //
                //recorremos todos los registros
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("usuario", rs.getString("nombre"));
                    datanum.put("UserId", rs.getString("UserId"));
                    datanum.put("imagen", rs.getString("imagen"));
                    data.add(datanum);
                }

                progressBar.dismiss();
            }
            catch(Exception err)
            {
                Log.w("Connection Error",err.getMessage());
            }
            return null;
        }
    }
}
