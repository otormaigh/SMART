package ie.teamchile.smartapp.activities.ParityDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.activities.Base.BaseActivity;
import ie.teamchile.smartapp.model.ResponseBaby;
import ie.teamchile.smartapp.model.ResponsePregnancy;
import ie.teamchile.smartapp.model.ResponseServiceUser;
import ie.teamchile.smartapp.util.Constants;
import ie.teamchile.smartapp.util.DividerItemDecoration;
import io.realm.Realm;

public class ParityDetailsActivity extends BaseActivity implements ParityDetailsView {
    private BaseAdapter adapter;
    private String patientName, patientParity;
    private String sex_male = "ale", sex_female = "emale";
    private List<String> nameBaby = new ArrayList<>();
    private List<String> hospitalNumber = new ArrayList<>();
    private List<Date> dobBaby = new ArrayList<>();
    private List<String> genderBaby = new ArrayList<>();
    private List<String> gestationBaby = new ArrayList<>();
    private List<Integer> weightBaby = new ArrayList<>();
    private List<String> weightBabyInKg = new ArrayList<>();
    private List<String> birthMode = new ArrayList<>();
    private List<String> birthOutcome = new ArrayList<>();
    private List<String> dobStr = new ArrayList<>();
    private List<String> hearing = new ArrayList<>();
    private List<String> nbstList = new ArrayList<>();
    private List<String> vitKList = new ArrayList<>();
    private int orientation;
    private RecyclerView rvParity;
    private Realm realm;
    private ParityDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentForNav(R.layout.activity_parity_details);

        initViews();

        presenter = new ParityDetailsPresenterImp(this, new WeakReference<Activity>(ParityDetailsActivity.this));

        realm = presenter.getEncryptedRealm();

        patientName = realm.where(ResponseServiceUser.class).findFirst().getPersonalFields().getName();
        patientParity = realm.where(ResponseServiceUser.class).findFirst().getClinicalFields().getParity();
        setActionBarTitle(String.format(Constants.FORMAT_PARITY_DETAILS_TITLE, patientName, patientParity));

        List<ResponseBaby> responseBabyList = realm.where(ResponseBaby.class).findAll();

        for (int i = 0; i < responseBabyList.size(); i++) {
            nameBaby.add(responseBabyList.get(i).getName());
            hospitalNumber.add(responseBabyList.get(i).getHospitalNumber());
            dobBaby.add(responseBabyList.get(i).getDeliveryDateTime());
            genderBaby.add(responseBabyList.get(i).getGender());
            gestationBaby.add(realm.where(ResponsePregnancy.class).findAll().get(i).getGestation());
            weightBaby.add(responseBabyList.get(i).getWeight());
            //birthMode.add(putArrayToString(ResponseBase.getInstance().getPregnancies().get(i).getBirthMode()));
            birthOutcome.add(responseBabyList.get(i).getBirthOutcome());
            hearing.add(responseBabyList.get(i).getHearing());
            nbstList.add(responseBabyList.get(i).getNbst());
            vitKList.add(responseBabyList.get(i).getVitK());
        }

        dobStr = new ArrayList<>();

        for (int i = 0; i < dobBaby.size(); i++) {
            dobStr.add(dobBaby.get(i) + "\n");
        }

        for (int i = 0; i < weightBaby.size(); i++) {
            String kg = getGramsToKg(weightBaby.get(i));
            weightBabyInKg.add(kg);
        }

        switch (getScreenOrientation()) {
            case 1:                //ORIENTATION_PORTRAIT
                //portraitCode();
                setUpNewLayout();
                break;
            case 2:                //ORIENTATION_LANDSCAPE
                landscapeCode();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void initViews() {
        rvParity = (RecyclerView) findViewById(R.id.rv_parity);
    }

    private int getScreenOrientation() {
        Display getOrient = this.getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        this.orientation = orientation;
        return orientation;
    }

    private void landscapeCode() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvParity.setLayoutManager(layoutManager);
        rvParity.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        RecyclerView.Adapter rvAdapter = new MyRecycleAdapter(nameBaby, hospitalNumber, dobBaby, genderBaby, gestationBaby,
                weightBaby, birthMode, birthOutcome, hearing, nbstList, vitKList);
        rvParity.setAdapter(rvAdapter);
    }

    public String getGramsToKg(int grams) {
        double kg;
        kg = grams / 1000.0;
        return String.valueOf(kg);
    }

    private String formatArrayString(String toBeFormatted) {
        String formatedString = toBeFormatted
                .replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replace("\"", "")
                .trim();
        return formatedString;
    }

    private String formatWeight(String weightStr) {
        DecimalFormat df = new DecimalFormat("#.00");
        double weight = Double.parseDouble(weightStr);

        return df.format(weight);
    }

    private void setUpNewLayout() {
        ListView lvNew;
        ListView lvNew2;
        ListView lvNew3;
        ListView lvNew4;

        BaseAdapter newAdapter;

        lvNew = (ListView) findViewById(R.id.lv_parity_new);
        lvNew2 = (ListView) findViewById(R.id.lv_parity_new_2);
        lvNew3 = (ListView) findViewById(R.id.lv_parity_new_3);
        lvNew4 = (ListView) findViewById(R.id.lv_parity_new_4);
        dobStr = new ArrayList<>();

        for (int i = 0; i < dobBaby.size(); i++) {
            //dobStr.add(getDeliveryDate(dobBaby.get(i), 2));
        }

        for (int i = 0; i < nameBaby.size(); i++) {
            nameBaby.set(i, nameBaby.get(i) + " (" + genderBaby.get(i) + ")");
        }

        newAdapter = new NewListAdapter(this, R.layout.list_layout_parity, nameBaby, hospitalNumber, dobStr);
        lvNew.setAdapter(newAdapter);
        newAdapter = new NewListAdapter(this, R.layout.list_layout_parity, nameBaby, gestationBaby, weightBabyInKg);
        lvNew2.setAdapter(newAdapter);
        newAdapter = new NewListAdapter(this, R.layout.list_layout_parity, nameBaby, birthMode, birthOutcome);
        lvNew3.setAdapter(newAdapter);
        newAdapter = new NewListAdapter(this, R.layout.list_layout_parity, nameBaby, hearing, nbstList);
        lvNew4.setAdapter(newAdapter);
    }

    private class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder> {
        private List<String> nameBaby, hospitalNumber, genderBaby, gestationBaby,
                birthMode, birthOutcome, hearing, nbst, vitK;
        private List<Date> dobBaby;
        private List<Integer> weightBaby;

        public MyRecycleAdapter(List<String> nameBaby,
                                List<String> hospitalNumber,
                                List<Date> dobBaby,
                                List<String> genderBaby,
                                List<String> gestationBaby,
                                List<Integer> weightBaby,
                                List<String> birthMode,
                                List<String> birthOutcome,
                                List<String> hearing,
                                List<String> nbst,
                                List<String> vitK) {
            this.nameBaby = nameBaby;
            this.hospitalNumber = hospitalNumber;
            this.dobBaby = dobBaby;
            this.genderBaby = genderBaby;
            this.gestationBaby = gestationBaby;
            this.weightBaby = weightBaby;
            this.birthMode = birthMode;
            this.birthOutcome = birthOutcome;
            this.hearing = hearing;
            this.nbst = nbst;
            this.vitK = vitK;
        }

        @Override
        public MyRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_parity, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyRecycleAdapter.ViewHolder holder, int position) {
            //holder.tvDob.setText(getDeliveryDate(dobBaby.get(position), 0));
            holder.tvGest.setText(gestationBaby.get(position));
            holder.tvGender.setText(genderBaby.get(position));
            holder.tvWeight.setText(String.valueOf(weightBaby.get(position)));
            holder.tvMode.setText(birthMode.get(position));
            holder.tvOutcome.setText(birthOutcome.get(position));
            holder.tvName.setText(nameBaby.get(position));
            holder.tvHospitalNum.setText(hospitalNumber.get(position));
            holder.tvHearing.setText(hearing.get(position));
            holder.tvNbst.setText(nbst.get(position));
            holder.tvVitK.setText(vitK.get(position));
        }

        @Override
        public int getItemCount() {
            return hospitalNumber.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvDob, tvGest, tvGender, tvWeight, tvMode, tvOutcome,
                    tvHearing, tvNbst, tvVitK, tvName, tvHospitalNum;

            public ViewHolder(View view) {
                super(view);

                tvDob = (TextView) view.findViewById(R.id.tv_parity_dob);
                tvGest = (TextView) view.findViewById(R.id.tv_parity_gestation);
                tvGender = (TextView) view.findViewById(R.id.tv_parity_gender);
                tvWeight = (TextView) view.findViewById(R.id.tv_parity_weight);
                tvMode = (TextView) view.findViewById(R.id.tv_parity_birth_mode);
                tvOutcome = (TextView) view.findViewById(R.id.tv_parity_outcome);
                tvName = (TextView) view.findViewById(R.id.tv_parity_name);
                tvHospitalNum = (TextView) view.findViewById(R.id.tv_parity_hospital_number);
                tvHearing = (TextView) view.findViewById(R.id.tv_parity_hearing);
                tvNbst = (TextView) view.findViewById(R.id.tv_parity_nbst);
                tvVitK = (TextView) view.findViewById(R.id.tv_parity_vit_k);
            }
        }
    }

    private class NewListAdapter extends BaseAdapter {
        Context context;
        int layoutResource;
        List<String> nameList;
        List<String> list1;
        List<String> list2;

        public NewListAdapter(Context context,
                              int layoutResource,
                              List<String> nameList,
                              List<String> list1,
                              List<String> list2) {
            super();
            this.context = context;
            this.layoutResource = layoutResource;
            this.nameList = nameList;
            this.list1 = list1;
            this.list2 = list2;

        }

        @Override
        public int getCount() {
            return nameList.size();
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
            convertView = View.inflate(context, layoutResource, null);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_parity_name);
            TextView tvList1 = (TextView) convertView.findViewById(R.id.tv_parity_empty_1);
            TextView tvList2 = (TextView) convertView.findViewById(R.id.tv_parity_empty_2);

            tvName.setText(nameList.get(position));
            tvList1.setText(list1.get(position));
            tvList1.setText(list1.get(position));
            tvList2.setText(list2.get(position));
            return convertView;
        }
    }
}
