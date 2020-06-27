package com.example.mobiledatacolection.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryController;

public class BooleanWidget{
    private final LinearLayout screen;

    public BooleanWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryController fep, int version) {
        //super(context);
        this.screen = screen;
        String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        TextView tv1 = new TextView(context);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setText(name);
        tv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Since I can connect from multiple devices, we store each connection instance separately
                // any time that connectionsRef's value is null (i.e. has no children) I am offline
                FirebaseDatabase database = UtilsFirebase.getDatabase();
                final DatabaseReference myConnectionsRef = database.getReference("data");
                final DatabaseReference connectedRef = database.getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            DatabaseReference con = myConnectionsRef.push();
                            con.onDisconnect().setValue(charSequence.toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("BooleanWidget", "Listener was cancelled at .info/connected");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        RadioButton edq = new RadioButton(context);
        screen.addView(tv1);
        screen.addView(edq);
    }

    public LinearLayout getElement(){
        return screen;
    }
}
