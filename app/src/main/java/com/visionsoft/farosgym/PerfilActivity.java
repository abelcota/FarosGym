package com.visionsoft.farosgym;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Planeacion-PC on 26/09/2016.
 */

public class PerfilActivity  extends Fragment{

    ImageView imageView;
    byte[] byteArray;
    String encodedImage;
    ConnectionClass conexion = new ConnectionClass();
    int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.perfil_activity, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fab);

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BuscarImagen();
            }
        });

        return view;
    }

    //metodo para buscar la imagen en el dispositivo
    public void BuscarImagen() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(
                Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);

        } else {
            Toast.makeText(getContext(), "No activity found to perform this task",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            //Toast.makeText(getContext(), selectedImage.toString(), Toast.LENGTH_LONG).show();
            InputStream imageStream;
            try {
                imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);

            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
            if (originBitmap != null) {
                this.imageView.setImageBitmap(originBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                originBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                //Toast.makeText(getContext(), "Conversion Done", Toast.LENGTH_SHORT).show();

                try
                {
                    new UploadtoDB().execute();
                }
                catch (Exception ee){
                    Toast.makeText(getContext(), ee.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Ocurrio un error al subir la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Algo malo pasó",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    //clase para controlar el loguin
    public class UploadtoDB extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void r) {

        }

        @Override
        protected Void doInBackground(Void... params) {
            //subimos la imagen
            Connection conn = null;
            try {
                //realizamos la conexión
                conn = conexion.CONN();
                PreparedStatement ps;
                ps=conn.prepareStatement("update Usertbl set imagen = ? where Id = ?");
                ps.setString(1,encodedImage);
                ps.setString(2,"6");
                ps.executeUpdate();
                Toast.makeText(getContext(), "insertado correctamente",
                        Toast.LENGTH_SHORT).show();
            }
             catch(Exception err)
             {
                    Log.w("Connection Error",err.getMessage());
             }
            return null;
        }
    }
}
