package com.visionsoft.farosgym;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
/**
 * Created by Planeacion-PC on 23/09/2016.
 */

public class usuarios extends AsyncTask <List<usuarios>, Void, List> {
    int id;
    String usuario;
    String contraseña;
    String nombre;

    List<usuarios> users = new ArrayList<usuarios>();
    ConnectionClass conexion = new ConnectionClass();

    //constructor
    public usuarios(){
        super();
    }

    @Override
    public String toString() {
        return this.nombre ;
    }

    protected List<usuarios> doInBackground(List... params) {
        Connection conn = null;
        try
        {
            //realizamos la conexión
            conn = conexion.CONN();
            Log.w("Connection","open");
            String query = "select * from Usertbl";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //recorremos todos los registros
            while (rs.next())
            {
                usuarios u = new usuarios();
                u.id = rs.getInt("Id");
                u.usuario = rs.getString("UserId");
                u.contraseña = rs.getString("Password");
                u.nombre = rs.getString("nombre");
                users.add(u);
            }
            //cerramos conexion
            conn.close();
        }
        catch (Exception e)
        {
            Log.w(e.getMessage(), e);
        }

        return users;
    }

    protected void onPostExecute(List users) {
        // Result is here now, may be 6 different List type.
        this.users = users;
    }
}
