package com.example.mobiledatacolection.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobiledatacolection.MobileDataCollect;
import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.javarosa.core.model.IFormElement;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.form.api.FormEntryController;
import org.javarosa.form.api.FormEntryPrompt;

public class TextWidget{

  LinearLayout screen;
  @SuppressLint("ResourceAsColor")
  public TextWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version) {
    this.screen = screen;
    String name = form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
    TextView tv1 = new TextView(context);
    tv1.setTextColor(Color.BLACK);
    tv1.setTypeface(Typeface.DEFAULT_BOLD);
    tv1.setText(name);
    EditText edq = new EditText(context);
    edq.addTextChangedListener(new TextWatcher() {
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
            Log.w("TextWidget", "Listener was cancelled at .info/connected");
          }
        });
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });
    this.screen.addView(tv1);
    this.screen.addView(edq);
  }

  public LinearLayout getElement(){
    return screen;
  }

}