package com.example.mobiledatacolection.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.activities.FormActivity;
import com.example.mobiledatacolection.model.FormsFill;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;
import com.example.mobiledatacolection.sqlLite.crudOperations.CrudFormsFill;
import com.example.mobiledatacolection.utils.UtilsFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class FormsFillAdapter extends ArrayAdapter<FormsFill>{
    private final boolean state;
    private Context mContext;
    private DatabaseReference databaseReference;
        private List<FormsFill> formsFill = new ArrayList<>();

        public FormsFillAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<FormsFill> list, boolean state) {
            super(context, R.layout.list_item , list);
            mContext = context;
            formsFill = list;
            this.state = state;
        }
        public int getCount() {
            return formsFill.size();
        }

        public FormsFill getItem(FormsFill position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

            Button image = listItem.findViewById(R.id.delete);
            image.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
            image.setTag(position);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();

                    // Access the row position here to get the correct data item
                    FormsFill form = getItem(position);
                    // Do what you want here...
                    SQLLiteDBHelper sqlLite = new SQLLiteDBHelper(getContext());
                    // clean forms fill
                    new CrudFormsFill(sqlLite).delete(form);
                    formsFill.remove(getItem(position));
                    notifyDataSetChanged();
                    //clean data in Firebase
                    databaseReference = UtilsFirebase.getDatabase()
                            .getReference("/company/" + form.getCompany() + "/user/"+form.getUser()+"/"+form.getFilename().split("\\.")[0]+"/"+form.getCreatedon()+"/");

                    databaseReference.removeValue();
                }
            });

            FormsFill currentFormsFill = formsFill.get(position);
            TextView filename = (TextView) listItem.findViewById(R.id.textView_filename);
            filename.setText(currentFormsFill.getFilename().split("\\.")[0]);

            TextView date = (TextView) listItem.findViewById(R.id.textView_createdon);
            date.setText(currentFormsFill.getCreatedon());

            TextView state1 = (TextView) listItem.findViewById(R.id.textView_state);
            state1.setText(currentFormsFill.getState());

            Button btButton = (Button) listItem.findViewById(R.id.button_edit);
            btButton.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
            // Cache row position inside the button using `setTag`
            btButton.setTag(position);
            // Attach the click event handler
            btButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    // Access the row position here to get the correct data item
                    FormsFill formsFill = getItem(position);
                    // Do what you want here...


                    Intent intent = new Intent(getContext(), FormActivity.class);
                    intent.putExtra("name_of_file", formsFill.getFilename());
                    intent.putExtra("COMPANY", formsFill.getCompany());
                    intent.putExtra("USERNAME", formsFill.getUser());
                    intent.putExtra("VERSION", formsFill.getVersion());
                    intent.putExtra("CREATEDON", formsFill.getCreatedon());

                 mContext.startActivity(intent);
                }
            });

            return listItem;
        }
}
