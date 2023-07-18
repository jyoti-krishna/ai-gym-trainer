package com.example.fitbudi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitbudi.ml.BmiModel1;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity2 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    int wt=0,ag=18,nec,kne,bic,wri,ank,ab,che,hi,thi;
    float fatpage=0.0f;
    String urnamestr=null;
    DBhelper DB;
    DBhelperprofile db;
    Cursor cursor;
    CardView c1,c2;
    Button addinfo;
    TextView calories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        DB=new DBhelper(this);
        db=new DBhelperprofile(this);
        cursor= db.getdata();
        cursor.moveToLast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#C4ACF2"));
        }
        if(cursor.getCount()>0)
            addinfo.setText("hello " + cursor.getString(0));

//        else{
//            TapTargetView.showFor(this,
//                    TapTarget.forView(addinfo,"WELCOME","first add your informations..")
//                            .outerCircleColor(R.color.teal_200)
//                            .outerCircleAlpha(0.96f)
//                            .targetCircleColor(R.color.white)
//                            .titleTextSize(19)
//                            .titleTextColor(R.color.black)
//                            .descriptionTextSize(18)
//                            .descriptionTextColor(R.color.black)
//                            .textColor(R.color.black)
//                            .textTypeface(Typeface.SANS_SERIF)
//                            .dimColor(R.color.black)
//                            .drawShadow(true)
//                            .cancelable(false)
//                            .tintTarget(true)
//                            .transparentTarget(true)
//                            .targetRadius(60),
//                    new TapTargetView.Listener(){
//                        @Override
//                        public void onTargetClick(TapTargetView view) {
//                            super.onTargetClick(view);
//                            openDialog();
//                        }
//                    }
//            );
//        }
        getPermission();
        CardView c1= findViewById(R.id.button);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        CardView c2= findViewById(R.id.button4);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button addinfo= findViewById(R.id.addinfo);
        addinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }
    public void openDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet);

        dialog.show();
        Button predict=(Button)dialog.findViewById(R.id.predict);
        Button saveinfo=(Button)dialog.findViewById(R.id.saveinfo);
        EditText weight=(EditText) dialog.findViewById(R.id.weight);
        EditText age=(EditText) dialog.findViewById(R.id.age);
        EditText edittextDescription =  (EditText)dialog.findViewById(R.id.urname);
        EditText ht=(EditText) dialog.findViewById(R.id.height);
        EditText neck=(EditText) dialog.findViewById(R.id.neck);
        EditText knees=(EditText) dialog.findViewById(R.id.knees);
        EditText biceps=(EditText) dialog.findViewById(R.id.biceps);
        EditText wrist=(EditText) dialog.findViewById(R.id.wrist);
        EditText ankle=(EditText) dialog.findViewById(R.id.ankle);
        EditText abd=(EditText) dialog.findViewById(R.id.abd);
        EditText chest=(EditText) dialog.findViewById(R.id.chest);
        EditText hip=(EditText) dialog.findViewById(R.id.hip);
        EditText thigh=(EditText) dialog.findViewById(R.id.thigh);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatpage=ml();
                if(fatpage>0.0f){
                    DB.insertdata(""+fatpage);
                    String outputString = String.format("%.2f", fatpage);
                    calories.setText(outputString);
                }
                dialog.dismiss();
            }
        });
        saveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(weight.getText().toString()=="" || age.getText().toString()=="" || edittextDescription.getText().toString()=="" || ht.getText().toString()=="" || wtloss.getText().toString()=="" || days.getText().toString()==""){
//                    Toast.makeText(MainActivity2.this, "profile not saved !!,Enter all values", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (edittextDescription.length() == 0) {
                    edittextDescription.setError("This field is required");
                    return ;
                }
                else if(edittextDescription.length() >=13){
                    edittextDescription.setError("should be shorter!!");
                }
                if (ht.length() == 0) {
                    ht.setError("This field is required");
                    return ;
                }
                if (weight.length() == 0) {
                    weight.setError("This field is required");
                    return ;
                }
//                else if(Integer.parseInt(weight.getText().toString())<20){
//                    wtloss.setError("ur weight is very less, eat something");
//                    return ;
//                }
                if (age.length() == 0) {
                    age.setError("This field is required");
                    return ;
                }
                else if(Integer.parseInt(age.getText().toString())<5 || Integer.parseInt(age.getText().toString())>110 ){
                    age.setError("u are either too young ur too old ");
                    return;
                }
                if (neck.length() == 0) {
                    neck.setError("This field is required");
                    return ;
                }
                if (knees.length() == 0) {
                    knees.setError("This field is required");
                    return ;
                }
                wt=Integer.parseInt(weight.getText().toString());
                ag=Integer.parseInt(age.getText().toString());

                nec=Integer.parseInt(neck.getText().toString());
                kne=Integer.parseInt(knees.getText().toString());
                bic=Integer.parseInt(biceps.getText().toString());
                wri=Integer.parseInt(wrist.getText().toString());
                ank=Integer.parseInt(ankle.getText().toString());
                ab=Integer.parseInt(abd.getText().toString());
                che=Integer.parseInt(chest.getText().toString());
                hi=Integer.parseInt(hip.getText().toString());
                thi=Integer.parseInt(thigh.getText().toString());

                urnamestr=edittextDescription.getText().toString();


                Boolean chkIns=db.insertdata(urnamestr,""+wt,""+ag,""+Float.parseFloat(ht.getText().toString()),""+nec,""+kne,""+bic,""+wri,""+ank,""+ab,""+che,""+hi,""+thi);

                if(chkIns==true) {
                    Toast.makeText(MainActivity2.this, "profile saved..", Toast.LENGTH_SHORT).show();
                    cursor= db.getdata();
                    cursor.moveToLast();
                    addinfo.setText("Hi!! " + cursor.getString(0));
                }
                else{
                    Toast.makeText(MainActivity2.this, "profile not saved!!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private float ml() {
        try {
            float[][] input={{}};
            BmiModel1 model = BmiModel1.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 10}, DataType.FLOAT32);
            if(cursor.getCount()>0) {
                cursor = db.getdata();
                cursor.moveToLast();
                float bmi = (float) ((float) (Float.parseFloat(cursor.getString(1))*2.2/Float.parseFloat(cursor.getString(3))*Float.parseFloat(cursor.getString(3)))*0.0001);
                float acrt=Float.parseFloat(cursor.getString(10))/Float.parseFloat(cursor.getString(11));
                float htr=Float.parseFloat(cursor.getString(12))/Float.parseFloat(cursor.getString(13));
                input= new float[][]{{Float.parseFloat(cursor.getString(2)), Float.parseFloat(cursor.getString(4)), Float.parseFloat(cursor.getString(5)), Float.parseFloat(cursor.getString(9)), Float.parseFloat(cursor.getString(6)), Float.parseFloat(cursor.getString(7)), Float.parseFloat(cursor.getString(8)),bmi, acrt, htr}};

            }

//            float[][] input = {{27,35.7f,36.7f,22.5f,29.9f,28.2f,17.7f,21.898919f,0.889509f,1.754545f}};
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * input.length * input[0].length);
            byteBuffer.order(ByteOrder.nativeOrder());
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[0].length; j++) {
                    byteBuffer.putFloat(input[i][j]);
                }
            }
            byteBuffer.rewind();
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            BmiModel1.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Releases model resources if no longer used.
            model.close();
            float[] outputValues = outputFeature0.getFloatArray();
            float opval=((495/outputValues[0])-490)/10;
            return opval;
        } catch (IOException e) {
            // TODO Handle the exception
        }
        return 0;
    }

    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA},102);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==102 && grantResults.length>0){
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                getPermission();
            }
        }
    }
}
