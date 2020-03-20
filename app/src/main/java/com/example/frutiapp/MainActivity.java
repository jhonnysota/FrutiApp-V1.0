package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_bestScore;
    private MediaPlayer mp;


    int num_aleatoria= (int)(Math.random()*4);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre=(EditText)findViewById(R.id.txtNombre);
        iv_personaje=(ImageView)findViewById(R.id.Iv_Personaje);
        tv_bestScore=(TextView)findViewById(R.id.tvScore);

        //poner una imagen en la parte superior
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //Proceso para cambia la imagen
        int id;

        switch (num_aleatoria) {
            case 0 :   id=getResources().getIdentifier("mango","drawable", getPackageName());
                iv_personaje.setImageResource(id);
                break;
            case 1   :id=getResources().getIdentifier("fresa","drawable", getPackageName());
                iv_personaje.setImageResource(id);
                break;
            case 2 :
                id=getResources().getIdentifier("manzana","drawable", getPackageName());
                iv_personaje.setImageResource(id);
            case 3:    id=getResources().getIdentifier("sandia","drawable", getPackageName());
                iv_personaje.setImageResource(id); break;

            case 4: id=getResources().getIdentifier("mango","drawable", getPackageName());
                iv_personaje.setImageResource(id); break;
                default:;
        }

        SQLLITE_CONSULTA();
        musica();

    }

    public void SQLLITE_CONSULTA(){
        AdminSQLiteOpenHelper admin=new AdminSQLiteOpenHelper(this,"BD",null,1);
        SQLiteDatabase BD=admin.getWritableDatabase();
        Cursor consulta=BD.rawQuery("select nombre, score from puntaje where score = (select max(score)from puntaje)",null);

        if(consulta.moveToFirst()){
            String tem_Nombre= consulta.getString(0);
            String tem_score=consulta.getString(1);
            tv_bestScore.setText("Record : "+ tem_score +" de "+ tem_Nombre);
            BD.close();
        }else{
            BD.close();
        }
    }

    public  void musica(){

        mp=MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);

    }



    public  void Jugar(View view){
        String nombre=et_nombre.getText().toString();

        if(!nombre.equals("")){
            mp.stop();
            mp.release();
            Intent i=new Intent(this,Main2Activity_nivel1.class);
            i.putExtra("jugador",nombre);
            startActivity(i);
            finish();
        }else {

            Toast.makeText(this,"Ingrese su nombre",Toast.LENGTH_LONG).show();
            et_nombre.requestFocus();
            InputMethodManager imm=(InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre,InputMethodManager.SHOW_IMPLICIT);
        }
    }




    @Override
    public  void  onBackPressed(){

    }
}
