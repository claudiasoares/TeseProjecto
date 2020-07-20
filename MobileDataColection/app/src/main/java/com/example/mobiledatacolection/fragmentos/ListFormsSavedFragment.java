package com.example.mobiledatacolection.fragmentos;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobiledatacolection.R;
import com.example.mobiledatacolection.adapters.FormsFillAdapter;
import com.example.mobiledatacolection.model.Forms;
import com.example.mobiledatacolection.model.FormsFill;
import com.example.mobiledatacolection.sqlLite.SQLLiteDBHelper;
import com.example.mobiledatacolection.sqlLite.crudOperations.CrudForms;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFormsSavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFormsSavedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DEBUG_TAG = "List Forms";
    private static final String DB = "forms";
    private static String user;
    private static SQLLiteDBHelper sqlhelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static List<FormsFill> files;
    private Button buttonSync;
    private Object server;
    private SharedPreferences preferencesServer;
    private FormsFillAdapter mAdapter;
    private ListView listView;
    private static boolean stateb ;

    public ListFormsSavedFragment() {
        // Required empty public constructor
    }

    private static String company;
    public static ListFormsSavedFragment newInstance(ArrayList<FormsFill> f, String c, String u, SQLLiteDBHelper sql, boolean statebuttons) {
        ListFormsSavedFragment fragment = new ListFormsSavedFragment();
        files=f;
        company = c;
        user = u;
        sqlhelper = sql;
        stateb = statebuttons;
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


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_formssaved, container, false);

        ArrayList<FormsFill> formsFills = new ArrayList<>();
        for (FormsFill form : files) {
            formsFills.add(form);
        }
        listView = (ListView) v.findViewById(R.id.list_files_save);

        mAdapter = new FormsFillAdapter(this.getActivity().getBaseContext(),formsFills,stateb);
        listView.setAdapter(mAdapter);
        return v;
    }


    private void saveFormsToDBSQLLite(List<Forms> listForms) throws Exception {
        new CrudForms(sqlhelper).write(listForms);
    }

    public Forms getForm (String filename, int version) throws Exception {
        return (Forms) new CrudForms(sqlhelper).read(filename,version);
    }







}