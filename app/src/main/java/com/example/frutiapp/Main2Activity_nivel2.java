package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_nivel2 extends AppCompatActivity {


    private TextView tv_nombre,tv_score;
    private ImageView iv_auno,ivAdos,iv_vidas;
    private EditText et_Resp;
    private MediaPlayer mp,mp_great,mp_bad;

    //region Variable para minutos
    Boolean isOn=true;
    TextView Tiempo;
    Thread cronos;
    int mili=0, seg=0, minutos=0;
    Handler h=new Handler();
    //endregion

    //variable
    int Score,numAleatoriouno,numAleatorio2,resultado,vidas=3;
    String nombre_jug,string_score,string_vidas;

    String numero[]={"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_nivel2);
        //icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        et_Resp=(EditText)findViewById(R.id.editText_Resultado);
        iv_auno=(ImageView)findViewById(R.id.imageViewNum1);
        ivAdos=(ImageView)findViewById(R.id.imageViewNum2);
        iv_vidas=(ImageView)findViewById(R.id.Vidas);
        tv_nombre=(TextView)findViewById(R.id.textViewNombre);
        tv_score=(TextView)findViewById(R.id.textView_score);
        Tiempo=(TextView)findViewById(R.id.Tiempo);

        ///traer datos del otro activitid
        nombre_jug=getIntent().getStringExtra("nombre");
        tv_nombre.setText("Jugador: "+ nombre_jug);

         string_score=getIntent().getStringExtra("score");
         Score=Integer.parseInt(string_score);
         tv_score.setText("Score: "+string_score);


         string_vidas=getIntent().getStringExtra("vidas");
         vidas = Integer.parseInt(string_vidas);

         switch (vidas) {
            case 3:
                iv_vidas.setImageResource(R.drawable.tresvidas);
                break;
            case 2:
                iv_vidas.setImageResource(R.drawable.dosvidas);
                break;
            case 1:
                iv_vidas.setImageResource(R.drawable.unavida);
                break;
                default:break;
        }

        Sonidos();
        numAleatorio();
        Toast.makeText(this,"Nivel 2 - Sumas Moderada "+ " Jugador: "+nombre_jug ,Toast.LENGTH_LONG).show();





    }
    public  void Sonidos(){
        //musica
        mp=MediaPlayer.create(this,R.raw.goats);
        mp.start();
        mp.setLooping(true);

        mp_great=MediaPlayer.create(this,R.raw.wonderful);
        mp_bad=MediaPlayer.create(this,R.raw.bad);
    }

    public void comprobar(View view){

        String Respuesta= et_Resp.getText().toString();
        if(!Respuesta.equals("")){

            if(Integer.parseInt(Respuesta)==resultado){
                mp_great.start();
                Score++;
                tv_score.setText("Score: "+Score);
                et_Resp.setText("");
                basededatos();
                Toast.makeText(this ,"Muy Bien",Toast.LENGTH_SHORT).show();
                numAleatorio();

            }else{
                mp_bad.start();
                vidas--;
                basededatos();
                switch (vidas){
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    default:
                        Toast.makeText(this,"Perdio toda su vida", Toast.LENGTH_LONG);
                        basededatos();
                        Intent i=new Intent(this,MainActivity.class);
                        startActivity(i);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                }
                Toast.makeText(this ,"Intentelo otra vez",Toast.LENGTH_SHORT).show();
                et_Resp.setText("");
            }
        }
        else{
            Toast.makeText(this ,"Escribe tu respuesta",Toast.LENGTH_SHORT).show();
            et_Resp.requestFocus();
            InputMethodManager imm=(InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_Resp,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public  void numAleatorio(){
        if(Score<=20){
            numAleatoriouno=(int) (Math.random()*10);
            numAleatorio2=(int) (Math.random()*10);
            resultado=numAleatoriouno+numAleatorio2;
            Toast.makeText(this,String.valueOf(resultado),Toast.LENGTH_LONG).show();

                if(resultado>2) {
                    for (int i = 0; i < numero.length; i++) {
                        int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                        if (numAleatoriouno == i) {
                            iv_auno.setImageResource(id);
                        }
                        if (numAleatorio2 == i) {
                            ivAdos.setImageResource(id);
                        }
                    }
                }else{
                    numAleatorio();
                }
            }else{
            Intent i=new Intent(this,Main2Activity3.class);
            string_score=String.valueOf(Score);
            string_vidas=String.valueOf(vidas);
            i.putExtra("score",string_score);
            i.putExtra("nombre",nombre_jug);
            i.putExtra("vidas",string_vidas);
            startActivity(i);
            finish();
            mp.stop();
            mp.release();
        }
    }

    public  void basededatos(){
        AdminSQLiteOpenHelper admin=new AdminSQLiteOpenHelper(this,"BD",null,1);
        SQLiteDatabase BD=admin.getWritableDatabase();
        Cursor consulta=BD.rawQuery("select * from puntaje where score = (select max(score)from puntaje) ",null);
        if(consulta.moveToFirst()) {
            String tem_nombre=consulta.getString(0);
            String tem_score=consulta.getString(1);

            int bestScore=Integer.parseInt(tem_score);
            if(Score>bestScore){

                ContentValues modificacion=new ContentValues();
                modificacion.put("nombre",nombre_jug);
                modificacion.put("score",Score);

                BD.update("puntaje", modificacion,"score = "+bestScore,null);
                BD.close();


            }
        }else{
            ContentValues insertar=new ContentValues();
            insertar.put("nombre",nombre_jug);
            insertar.put("score",Score);

            BD.insert("puntaje", null,insertar);
            BD.close();

        }
    }

    public  void PerdistePorTiempo(){

        if(minutos==06){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
            mp.stop();
            mp.release();

        }
    }

    //programar el boton de retroceso
    @Override
    public  void  onBackPressed(){
        mp.stop();
        mp.release();
        basededatos();
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();

    }
}
