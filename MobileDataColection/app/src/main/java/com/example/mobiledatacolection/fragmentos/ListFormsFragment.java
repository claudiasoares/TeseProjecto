package com.example.mobiledatacolection.fragmentos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.activities.FormActivity;
import com.example.mobiledatacolection.activities.MenuActivity;
import com.example.mobiledatacolection.adapters.WidgetFragmentCollectionAdapter;
import com.example.mobiledatacolection.http.HttpGetRequest;
import com.example.mobiledatacolection.model.Forms;
import com.example.mobiledatacolection.model.User;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;
import com.example.mobiledatacolection.utils.FileUtils;
import com.example.mobiledatacolection.widget.WidgetFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1beta1.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFormsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DEBUG_TAG = "List Forms";
    private static final String DB = "forms";
    private static String user;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static List<Forms> files;
    private Button buttonSync;
    private Object server;
    private SharedPreferences preferencesServer;
    private ListView lv;

    public static final String FORMS_TABLE_NAME = "forms";
    public static final String FORMS_COLUMN_ID = "_id";
    public static final String FORMS_COLUMN_FILENAME = "filename";
    public static final String FORMS_COLUMN_VERSION = "version";
    public static final String FORMS_COLUMN_COMPANY = "company";
    public static final String FORMS_COLUMN_CATEGORY = "category";
    public static final String FORMS_COLUMN_FILE = "file";

    public ListFormsFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static String company;
    public static ListFormsFragment newInstance(ArrayList<Forms> f, String c, String u) {
        ListFormsFragment fragment = new ListFormsFragment();
        files=f;
        company = c;
        user = u;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        View view;
        preferencesServer = getContext().getSharedPreferences("com.example.mobiledatacolection_preferences", MODE_PRIVATE);
        server = preferencesServer.getString("server","");
        //Button synchronization =  (Button) findViewById(R.id.synchro);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_forms, container, false);
        lv = (ListView) v.findViewById(R.id.list_files);
        buttonSync = v.findViewById(R.id.synchro);
        buttonSync.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              syncForm(company);
                                          }
                                      }
        );
        showFormsName();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,      int pos, long id) {
               String fileName = ((TextView)view).getText().toString();
               Intent intent = new Intent(getContext(), FormActivity.class);
               intent.putExtra("name_of_file", fileName);
               intent.putExtra("COMPANY", company);
               intent.putExtra("USERNAME", user);
               startActivity(intent);
            }
        });

        return v;
    }

    private void showFormsName(){
        final List<String> fi = new ArrayList<String>();
        for (Forms f : files) {
            fi.add(f.getFilename());
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.text_list_form, fi);
        lv.setAdapter(arrayAdapter);
    }

    private void syncForm(String company) {
        if(hasConectivity()){
            getFormsFromServer(company);
            showFormsName();
        }else{

            Toast.makeText(getContext(),"There is not connectivity!",Toast.LENGTH_SHORT);
        }

    }

    private void getFormsFromServer(String company) {
        // Instantiate the RequestQueue.
        //Some url endpoint that you may have
        String myUrl = server + "api/allforms/" + company;
        //String to place our result in
        String result;
        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(myUrl).get();
            JSONObject obj = new JSONObject(result);
            JSONArray forms = obj.getJSONArray("form");
            List<Forms> listForms = Forms.parseFormData(forms, getContext());
            for(int i = 0; i < listForms.size(); ++i) {
                if (!files.contains(listForms.get(i))) {
                    files.add(listForms.get(i));
                }
            }
           // saveFormsToDB(listForms);
            saveFormsToDBSQLLite(listForms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFormsToDBSQLLite(List<Forms> listForms) {

        SQLiteDatabase database = new SQLLiteDBHelper(getContext()).getWritableDatabase();

        for (int i =0; i < listForms.size(); ++i){
            ContentValues values = new ContentValues();
            values.put(SQLLiteDBHelper.FORMS_COLUMN_FILENAME, listForms.get(i).getFilename());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_COMPANY, listForms.get(i).getCompany());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_CATEGORY, listForms.get(i).getCategory());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_VERSION, listForms.get(i).getVersion());
            values.put(SQLLiteDBHelper.FORMS_COLUMN_FILE, listForms.get(i).getFiles());
            long newRowId = database.insert(SQLLiteDBHelper.FORMS_TABLE_NAME, null, values);
        }

    }

    private void saveFormsToDB(List<Forms> listForms) {
        for (int i = 0; i< listForms.size(); ++i){
            // ver se a base de dados tem o ficheiro se não tiver inserir
            // Write a message to the database
            // ver se esta na lista
            if(!files.contains(listForms.get(i))){
                files.add(listForms.get(i));
            }
        }
    }

    public Forms getForm (String filename, int version){
        SQLiteDatabase database = new SQLLiteDBHelper(getContext()).getWritableDatabase();
        String[] columns = new String[]{FORMS_COLUMN_ID, FORMS_COLUMN_CATEGORY, FORMS_COLUMN_COMPANY, FORMS_COLUMN_VERSION, FORMS_COLUMN_FILE};
        Cursor cursor = database.query(FORMS_TABLE_NAME, allColumns, DB.ID + " = " +
                idContacto, null,null, null, null);
        cursor.moveToFirst();
        return cursorToContacto(cursor);
    }

  /*  public void readDB(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG= "Forms";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }*/

    private boolean hasConectivity() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);
        return isMobileConn || isWifiConn;
    }


}