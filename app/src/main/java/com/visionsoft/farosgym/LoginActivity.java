package com.visionsoft.farosgym;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Abel Cota el 20/09/2016.
 */

public class LoginActivity extends Activity {

    final Context context = this;
    ConnectionClass connectionClass;
    EditText edtuserid, edtpass;
    Button btnlogin;
    TextView link;
    private  ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        connectionClass = new ConnectionClass();
        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        link = (TextView) findViewById(R.id.link_signup);

        link.setOnClickListener(new View.OnClickListener()
        {
          @Override
           public void onClick(View v)
          {
              final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
              // set title
              alertDialogBuilder.setTitle("Aviso");
              // set dialog message
              alertDialogBuilder
                      .setMessage("¿Desea crear una cuenta?")
                      .setCancelable(false)
                      .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,int id) {
                              Intent myIntent = new Intent(LoginActivity.this, RegistroActivity.class);
                              //myIntent.putExtra("key", value); //Optional parameters
                              LoginActivity.this.startActivity(myIntent);

                              finish();
                          }
                      })
                      .setNegativeButton("No",new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,int id) {
                             dialog.cancel();
                          }
                      }) ;

              //Toast.makeText(LoginActivity.this,r,Toast.LENGTH_SHORT).show();
              // create alert dialog
              AlertDialog alertDialog = alertDialogBuilder.create();
              alertDialog.show();
          }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Iniciando sesión ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });
    }
    //clase para controlar el loguin
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String mensaje = "";
        Boolean isSuccess = false;
        String id_usuario;
        String imagen;
        String nombre_usuario;

        //se obtienen los valores de las cajas de texto
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();

        @Override
        protected void onPreExecute() {

            //pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Faro's Gym");
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

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            if(isSuccess) {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                //pasamos el userid
                myIntent.putExtra("user", nombre_usuario.toString()); //Optional parameters
                myIntent.putExtra("id_user", id_usuario.toString()); //Optional parameters
                //myIntent.putExtra("imagen", imagen.toString()); //Optional parameters
                LoginActivity.this.startActivity(myIntent);

                finish();
            }
            else
            {
                // show it
                alertDialog.show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(userid.trim().equals("")|| password.trim().equals(""))
                mensaje = "Por favor ingrese su usuario y contraseña";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        mensaje = "Error al conectar con el servidor";
                    } else {
                        String query = "select * from Usertbl where UserId='" + userid + "' and password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {
                            id_usuario = rs.getString("Id");
                            nombre_usuario = rs.getString("nombre");
                            imagen = rs.getString("imagen");
                            mensaje = "Bienvenido";
                            isSuccess=true;
                        }
                        else
                        {
                            mensaje = "Usuario o Contraseña incorrecto";
                            isSuccess = false;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    mensaje = "Errores";
                }
            }
            return mensaje;
        }
    }
}