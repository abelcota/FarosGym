package com.visionsoft.farosgym;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Planeacion-PC on 21/09/2016.
 */

public class RegistroActivity extends Activity {

    final Context context = this;
    ConnectionClass connectionClass;
    private  ProgressDialog progressBar;
    private static final String TAG = "SignupActivity";
    EditText input_usuario, input_password, input_name;
    Button btn_signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);
        connectionClass = new ConnectionClass();
        input_name = (EditText) findViewById(R.id.input_name);
        input_password = (EditText) findViewById(R.id.input_password);
        input_usuario = (EditText) findViewById(R.id.input_usuario) ;
        btn_signup = (Button) findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Creando usuario. Espere...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
                DoCreate doCreate = new DoCreate();
                doCreate.execute("");
            }
        });

    }
    //clase para controlar el loguin
    public class DoCreate extends AsyncTask<String,String,String> {

        String mensaje = "";
        Boolean isSuccess = false;
        //se obtienen los valores de las cajas de texto
        String nombre = input_name.getText().toString();
        String usuario = input_usuario.getText().toString();
        String contraseña = input_password.getText().toString();
        //por si acaso se llega a ocupar
        @Override
        protected void onPreExecute() {

            //code here
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Registro");
            // set dialog message
            alertDialogBuilder
                    .setMessage(r)
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            //Toast.makeText(LoginActivity.this,r,Toast.LENGTH_SHORT).show();
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            if(isSuccess) {
                alertDialog.show();
                //abrimos el login
                Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
            else
            {
                // show it
                alertDialog.show();
            }

        }
        //insertamos el registro en la base de datos
        @Override
        protected String doInBackground(String... params) {
            if(nombre.trim().equals("")|| usuario.trim().equals("") || contraseña.trim().equals(""))
                mensaje = "Por favor ingrese todos sus datos";
            else
            {
                try
                {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        mensaje = "Error al conectar con el servidor";
                    }
                    else
                    {
                        PreparedStatement ps;
                        ps=con.prepareStatement("insert into Usertbl values(?,?,?,?)");
                        ps.setString(1,usuario);
                        ps.setString(2,contraseña);
                        ps.setString(3,nombre);
                        ps.setString(4,"iVBORw0KGgoAAAANSUhEUgAAADsAAAA7CAIAAABKR2XkAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABSSURBVGhD7c5BDQAwEASh+je9VcHjkkEBb9c09hp7jb3GXmOvsdfYa+w19hp7jb3GXmOvsdfYa+w19hp7jb3GXmOvsdfYa+w19hp7jb3GXmNt+yb9pI5+2AU+AAAAAElFTkSuQmCC");
                        int x;
                        x=ps.executeUpdate();
                        mensaje = "Usuario registrado correctamente";
                        Log.d("respuesta", "Value: " + x);
                        isSuccess=true;
                    }

                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    mensaje = ex.getMessage();
                }
            }

            return mensaje;
        }
    }
}

