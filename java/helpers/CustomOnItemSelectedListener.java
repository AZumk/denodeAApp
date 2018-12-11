package helpers;

/**
 * Created by Ana Zumk on 20/10/2017.
 */

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import app.denode.denode.addTarefa_activity;

import static app.denode.denode.R.id.addTarefa;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Object selected =  parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}