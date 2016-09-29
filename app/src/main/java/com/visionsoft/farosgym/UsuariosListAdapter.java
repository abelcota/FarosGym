package com.visionsoft.farosgym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Planeacion-PC on 23/09/2016.
 */

public class UsuariosListAdapter extends BaseAdapter {
    Context context = null;
    List<usuarios> postUsuarios = new ArrayList<>();
    LayoutInflater inflater;

    public UsuariosListAdapter(Context context, int fragment_usuarios, List<usuarios> postUsuarios){
        this.context = context;
        this.postUsuarios = postUsuarios;
    }

    @Override
    public int getCount() {
        return postUsuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.drawe_item, parent, false);
        usuarios u = postUsuarios.get(position);
        if (u != null) {
            TextView nombre = (TextView) v.findViewById(R.id.title);
            TextView usuario = (TextView) v.findViewById(R.id.subTitle);

            if (nombre != null) {
                nombre.setText(u.nombre);
            }
            if (usuario != null) {

                usuario.setText(u.usuario);
            }
        }
        return v;
    }
}
