package com.navarra.dya.encierro;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import static com.navarra.dya.encierro.CommonUtilities.TAG_AMBULANCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BLOOD_PRESSURE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BREAK_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_BRUISES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONSCIOUS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_FACE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GENDER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GLASGOW;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_BACK;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_GORING_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_HEALTH_CENTER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_HEMORRHAGES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_ID_INJURED;
import static com.navarra.dya.encierro.CommonUtilities.TAG_NOTES;
import static com.navarra.dya.encierro.CommonUtilities.TAG_OLD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PENETRATING_INJURY_ABDOMEN;
import static com.navarra.dya.encierro.CommonUtilities.TAG_PENETRATING_INJURY_TX;
import static com.navarra.dya.encierro.CommonUtilities.TAG_STAND;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TCE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRACHEAL_INTUBATION;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRANSFER;
import static com.navarra.dya.encierro.CommonUtilities.TAG_TRIAGE;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_BACK;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_CHEST;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_HEAD;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_DEFORMITY_ARMS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CONTUSION_DEFORMITY_LEGS;
import static com.navarra.dya.encierro.CommonUtilities.TAG_CUTS;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class SpecialAdapter extends SimpleAdapter {
    private int[] colors = new int[] { 0x30ffffff, 0x30808080 };
    View rowView;
    private final Context context;
    private ArrayList<HashMap<String, String>> items;

    public SpecialAdapter(Context context, ArrayList<HashMap<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
        this.context = context;
        this.items=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.list_injured, parent, false);
        HashMap<String, String> map = items.get(position);

            TextView txtId= (TextView) rowView.findViewById(R.id.iid);
            TextView txtStand= (TextView) rowView.findViewById(R.id.stand);
            TextView txtGender= (TextView) rowView.findViewById(R.id.gender);
            TextView txtAmbulance= (TextView) rowView.findViewById(R.id.ambulance);
            TextView txtHealthCenter= (TextView) rowView.findViewById(R.id.health_center);
            TextView txtOld= (TextView) rowView.findViewById(R.id.old);
            TextView txtConcious= (TextView) rowView.findViewById(R.id.txtConscious);
            TextView txtSymptoms= (TextView) rowView.findViewById(R.id.txtSymtoms);
            TextView txtGoringBack= (TextView) rowView.findViewById(R.id.txtGoringBack);
            TextView txtGoringHead= (TextView) rowView.findViewById(R.id.txtGoringHead);
            TextView txtGoringLegs= (TextView) rowView.findViewById(R.id.txtGoringLegs);
            TextView txtGoringArms= (TextView) rowView.findViewById(R.id.txtGoringArms);
            TextView txtBreakHead= (TextView) rowView.findViewById(R.id.txtBreakHead);
            TextView txtBreakArms= (TextView) rowView.findViewById(R.id.txtBreakArms);
            TextView txtBreakLegs= (TextView) rowView.findViewById(R.id.txtBreakLegs);
            TextView txtTce= (TextView) rowView.findViewById(R.id.txtTce);
            TextView txtHemorrhages= (TextView) rowView.findViewById(R.id.txtHemorrhages);
            TextView txtBruises= (TextView) rowView.findViewById(R.id.txtBruises);
            TextView txtFace= (TextView) rowView.findViewById(R.id.txtFace);
            TextView txtAdvanced= (TextView) rowView.findViewById(R.id.txtAdvanced);
            TextView txtGlasgow= (TextView) rowView.findViewById(R.id.txtGlasgow);
            TextView lblGlasgow= (TextView) rowView.findViewById(R.id.lblGlasgow);
            TextView txtBloodPresure= (TextView) rowView.findViewById(R.id.txtBloodPresure);
            TextView lblBloodPresure= (TextView) rowView.findViewById(R.id.lblBloodPresure);
            TextView txtPenetratingInjuryTx= (TextView) rowView.findViewById(R.id.txtPenetratingInjuryTx);
            TextView txtPenetratingInjuryAbdomen= (TextView) rowView.findViewById(R.id.txtPenetratingInjuryAbdomen);
            ImageView imgPenetratingInjuryTx= (ImageView) rowView.findViewById(R.id.imgPenetratingInjuryTx);
            ImageView imgPenetratingInjuryAbdomen=(ImageView) rowView.findViewById(R.id.imgPenetratingInjuryAbdomen);
            TextView txtTrachealIntubation= (TextView) rowView.findViewById(R.id.txtTrachealIntubation);
            TextView lblTrachealIntubation= (TextView) rowView.findViewById(R.id.lblTrachealIntubation);
            TextView lblNotes= (TextView) rowView.findViewById(R.id.lblNotes3);
            TextView txtNotes= (TextView) rowView.findViewById(R.id.txtNotes);
            TextView txtContusionHead= (TextView) rowView.findViewById(R.id.txtContusionHead);
            TextView txtContusionChest= (TextView) rowView.findViewById(R.id.txtContusionChest);
            TextView txtContusionBack= (TextView) rowView.findViewById(R.id.txtContusionBack);
            TextView txtContusionLegs= (TextView) rowView.findViewById(R.id.txtContusionLegs);
            TextView txtContusionArms= (TextView) rowView.findViewById(R.id.txtContusionArms);
            TextView txtContusionDeformityArms = (TextView) rowView.findViewById(R.id.txtContusionDeformityArms);
            TextView txtContusionDeformityLegs = (TextView) rowView.findViewById(R.id.txtContusionDeformityLegs);
            TextView txtCuts = (TextView) rowView.findViewById(R.id.txtCuts);

            ImageView imgTriage= (ImageView) rowView.findViewById(R.id.imgTriage);
            ImageView imgGoringHead=(ImageView) rowView.findViewById(R.id.imgGoringHead);
            ImageView imgGoringLegs=(ImageView) rowView.findViewById(R.id.imgGoringLegs);
            ImageView imgGoringArms=(ImageView) rowView.findViewById(R.id.imgGoringArms);
            ImageView imgGoringBack=(ImageView) rowView.findViewById(R.id.imgGoringBack);
            ImageView imgBreakHead=(ImageView) rowView.findViewById(R.id.imgBreakHead);
            ImageView imgBreakArms=(ImageView) rowView.findViewById(R.id.imgBreakArms);
            ImageView imgBreakLegs=(ImageView) rowView.findViewById(R.id.imgBreakLegs);
            ImageView imgConscious=(ImageView) rowView.findViewById(R.id.imgConscious);
            ImageView imgTce=(ImageView) rowView.findViewById(R.id.imgTce);
            ImageView imgHemorrhages=(ImageView) rowView.findViewById(R.id.imgHemorrhages);
            ImageView imgBruises=(ImageView) rowView.findViewById(R.id.imgBruises);
            ImageView imgAmbulance=(ImageView) rowView.findViewById(R.id.imgAmbulance);
            ImageView imgHospital=(ImageView) rowView.findViewById(R.id.imgHopital);
            ImageView imgContusionHead=(ImageView) rowView.findViewById(R.id.imgContusionHead);
            ImageView imgContusionChest=(ImageView) rowView.findViewById(R.id.imgContusionChest);
            ImageView imgContusionBack=(ImageView) rowView.findViewById(R.id.imgContusionBack);
            ImageView imgContusionLegs=(ImageView) rowView.findViewById(R.id.imgContusionLegs);
            ImageView imgContusionArms=(ImageView) rowView.findViewById(R.id.imgContusionArms);
            ImageView imgDeformitiesArms=(ImageView) rowView.findViewById(R.id.imgContusionDeformityArms);
            ImageView imgDeformitiesLegs=(ImageView) rowView.findViewById(R.id.imgContusionDeformityLegs);
            ImageView imgCuts=(ImageView) rowView.findViewById(R.id.imgCuts);

            String ambulance= map.get(TAG_AMBULANCE);
            String health_center= map.get(TAG_HEALTH_CENTER);
            String conscious= map.get(TAG_CONSCIOUS);
            String goringHead= map.get(TAG_GORING_HEAD);
            String goringBack= map.get(TAG_GORING_BACK);
            String goringArms= map.get(TAG_GORING_ARMS);
            String goringLegs= map.get(TAG_GORING_LEGS);
            String breakHead= map.get(TAG_BREAK_HEAD);
            String breakArms= map.get(TAG_BREAK_ARMS);
            String breakLegs= map.get(TAG_BREAK_LEGS);
            String tce= map.get(TAG_TCE);
            String hemorrhages= map.get(TAG_HEMORRHAGES);
            String bruises= map.get(TAG_BRUISES);
            String face= map.get(TAG_FACE);
            String triage= map.get(TAG_TRIAGE);
            String glasgow= map.get(TAG_GLASGOW);
            String bloodPresure= map.get(TAG_BLOOD_PRESSURE);
            String penetratingInjutyTx= map.get(TAG_PENETRATING_INJURY_TX);
            String penetratingInjutyAbdomen= map.get(TAG_PENETRATING_INJURY_ABDOMEN);
            String trachealIntubation= map.get(TAG_TRACHEAL_INTUBATION);
            String notes= map.get(TAG_NOTES);
            String transfer= map.get(TAG_TRANSFER);
            String contusionHead= map.get(TAG_CONTUSION_HEAD);
            String contusionBack= map.get(TAG_CONTUSION_BACK);
            String contusionChest= map.get(TAG_CONTUSION_CHEST);
            String contusionArms= map.get(TAG_CONTUSION_ARMS);
            String contusionLegs= map.get(TAG_CONTUSION_LEGS);
            String contusionDeformityArms= map.get(TAG_CONTUSION_DEFORMITY_ARMS);
            String contusionDeformityLegs= map.get(TAG_CONTUSION_DEFORMITY_LEGS);
            String cuts= map.get(TAG_CUTS);

            txtId.setVisibility(View.VISIBLE);
            txtStand.setVisibility(View.VISIBLE);
            txtGender.setVisibility(View.VISIBLE);
            txtOld.setVisibility(View.VISIBLE);
            txtConcious.setVisibility(View.VISIBLE);

            imgTriage.setVisibility(View.VISIBLE);
        /*
        0: Negro
        1: Rojo
        2: Naranja
        3: Amarillo
        4: Verde
        5: Azul
        6: blanca
          */
            if (triage.equals("6")) {
                imgTriage.setImageResource(R.drawable.flagwhite);
            }else if (triage.equals("5")){
                    imgTriage.setImageResource(R.drawable.flagblue);
            }else if (triage.equals("4")){
                imgTriage.setImageResource(R.drawable.flaggreen);
            }else if (triage.equals("3")){
                imgTriage.setImageResource(R.drawable.flagyellow);
            }else if (triage.equals("2")){
                imgTriage.setImageResource(R.drawable.flagorange);
            }else if (triage.equals("1")){
                imgTriage.setImageResource(R.drawable.flagred);
            }else if (triage.equals("0")){
                imgTriage.setImageResource(R.drawable.flagblack);
            }

            txtId.setText(map.get(TAG_ID_INJURED));
            txtStand.setText(map.get(TAG_STAND));
            txtGender.setText(map.get(TAG_GENDER));
            txtOld.setText(map.get(TAG_OLD));
            txtConcious.setText(map.get(TAG_CONSCIOUS));

        if (!ambulance.equalsIgnoreCase("")){
            txtAmbulance.setVisibility(View.VISIBLE);
            txtAmbulance.setText(ambulance);
            imgAmbulance.setVisibility(View.VISIBLE);
        }else{
            txtAmbulance.setVisibility(View.GONE);
            imgAmbulance.setVisibility(View.GONE);
        }
        if(transfer.equalsIgnoreCase("TRASLADO HOSPITAL")){
            imgAmbulance.setVisibility(View.VISIBLE);
        }
        if (!health_center.equalsIgnoreCase("")){
            txtHealthCenter.setVisibility(View.VISIBLE);
            txtHealthCenter.setText(health_center);
            imgHospital.setVisibility(View.VISIBLE);
        }else{
            txtHealthCenter.setVisibility(View.GONE);
            imgHospital.setVisibility(View.GONE);
        }
            txtConcious.setVisibility(View.VISIBLE);
            imgConscious.setVisibility(View.VISIBLE);
            txtConcious.setText(conscious);
        if (conscious.equalsIgnoreCase("CONSCIENTE")){
            imgConscious.setImageResource(R.drawable.green);
        }else{
            imgConscious.setImageResource(R.drawable.red);
        }
        if (!goringHead.equalsIgnoreCase("")){
            txtGoringHead.setVisibility(View.VISIBLE);
            imgGoringHead.setVisibility(View.VISIBLE);
            txtGoringHead.setText(goringHead);
        }else{
            txtGoringHead.setVisibility(View.GONE);
            imgGoringHead.setVisibility(View.GONE);
        }

        if (!goringArms.equalsIgnoreCase("")){
            txtGoringArms.setVisibility(View.VISIBLE);
            imgGoringArms.setVisibility(View.VISIBLE);
            txtGoringArms.setText(goringArms);
        }else{
            txtGoringArms.setVisibility(View.GONE);
            imgGoringArms.setVisibility(View.GONE);
        }
        if (!goringLegs.equalsIgnoreCase("")){
            txtGoringLegs.setVisibility(View.VISIBLE);
            imgGoringLegs.setVisibility(View.VISIBLE);
            txtGoringLegs.setText(goringLegs);
        }else{
            txtGoringLegs.setVisibility(View.GONE);
            imgGoringLegs.setVisibility(View.GONE);
        }
        if (!goringBack.equalsIgnoreCase("")){
            txtGoringBack.setVisibility(View.VISIBLE);
            imgGoringBack.setVisibility(View.VISIBLE);
            txtGoringBack.setText(goringBack);
        }else{
            txtGoringBack.setVisibility(View.GONE);
            imgGoringBack.setVisibility(View.GONE);
        }


        if (!penetratingInjutyTx.equalsIgnoreCase("")){
            txtPenetratingInjuryTx.setVisibility(View.VISIBLE);
            imgPenetratingInjuryTx.setVisibility(View.VISIBLE);
            txtPenetratingInjuryTx.setText(penetratingInjutyTx);
        }else{
            txtPenetratingInjuryTx.setVisibility(View.GONE);
            imgPenetratingInjuryTx.setVisibility(View.GONE);
        }
        if (!penetratingInjutyAbdomen.equalsIgnoreCase("")){
            txtPenetratingInjuryAbdomen.setVisibility(View.VISIBLE);
            imgPenetratingInjuryAbdomen.setVisibility(View.VISIBLE);
            txtPenetratingInjuryAbdomen.setText(penetratingInjutyAbdomen);
        }else{
            txtPenetratingInjuryAbdomen.setVisibility(View.GONE);
            imgPenetratingInjuryAbdomen.setVisibility(View.GONE);
        }


        if (!breakHead.equalsIgnoreCase("")){
            txtBreakHead.setVisibility(View.VISIBLE);
            imgBreakHead.setVisibility(View.VISIBLE);
            txtBreakHead.setText(breakHead);
        }else{
            txtBreakHead.setVisibility(View.GONE);
            imgBreakHead.setVisibility(View.GONE);
        }

        if (!breakArms.equalsIgnoreCase("")){
            txtBreakArms.setVisibility(View.VISIBLE);
            imgBreakArms.setVisibility(View.VISIBLE);
            txtBreakArms.setText(breakArms);
        }else{
            txtBreakArms.setVisibility(View.GONE);
            imgBreakArms.setVisibility(View.GONE);
        }
        if (!breakLegs.equalsIgnoreCase("")){
            txtBreakLegs.setVisibility(View.VISIBLE);
            imgBreakLegs.setVisibility(View.VISIBLE);
            txtBreakLegs.setText(breakLegs);
        }else{
            txtBreakLegs.setVisibility(View.GONE);
            imgBreakLegs.setVisibility(View.GONE);
        }
        if (!tce.equalsIgnoreCase("")){
            txtTce.setVisibility(View.VISIBLE);
            imgTce.setVisibility(View.VISIBLE);
            txtTce.setText(tce);
        }else{
            txtTce.setVisibility(View.GONE);
            imgTce.setVisibility(View.GONE);
        }
        if (!hemorrhages.equalsIgnoreCase("")){
            txtHemorrhages.setVisibility(View.VISIBLE);
            imgHemorrhages.setVisibility(View.VISIBLE);
            txtHemorrhages.setText(hemorrhages);
        }else{
            txtHemorrhages.setVisibility(View.GONE);
            imgHemorrhages.setVisibility(View.GONE);
        }

        //Mas informacion
        if(!glasgow.equalsIgnoreCase("") || !bloodPresure.equalsIgnoreCase("") || !trachealIntubation.equalsIgnoreCase("")|| !face.equalsIgnoreCase("") || !notes.equalsIgnoreCase(""))
            txtAdvanced.setVisibility(View.VISIBLE);
        else
            txtAdvanced.setVisibility(View.GONE);
        //Datos adicionales
        if (!glasgow.equalsIgnoreCase("")){
            txtGlasgow.setVisibility(View.VISIBLE);
            lblGlasgow.setVisibility(View.VISIBLE);
            txtGlasgow.setText(glasgow);
        }else{
            txtGlasgow.setVisibility(View.GONE);
            lblGlasgow.setVisibility(View.GONE);
        }
        if (!bloodPresure.equalsIgnoreCase("")){
            txtBloodPresure.setVisibility(View.VISIBLE);
            lblBloodPresure.setVisibility(View.VISIBLE);
            txtBloodPresure.setText(bloodPresure);
        }else{
            txtBloodPresure.setVisibility(View.GONE);
            lblBloodPresure.setVisibility(View.GONE);
        }
        if (!trachealIntubation.equalsIgnoreCase("")){
            txtTrachealIntubation.setVisibility(View.VISIBLE);
            lblTrachealIntubation.setVisibility(View.VISIBLE);
            //txtTrachealIntubation.setText("Si");
        }else{
            txtTrachealIntubation.setVisibility(View.GONE);
            lblTrachealIntubation.setVisibility(View.GONE);
        }
        if (!face.equalsIgnoreCase("")){
            txtFace.setVisibility(View.VISIBLE);
            txtFace.setText(face);
        }else{
            txtFace.setVisibility(View.GONE);
        }
        if (!notes.equalsIgnoreCase("")){
            txtNotes.setVisibility(View.VISIBLE);
            lblNotes.setVisibility(View.VISIBLE);
            txtNotes.setText(notes);
        }else{
            txtNotes.setVisibility(View.GONE);
            lblNotes.setVisibility(View.GONE);
        }

        if (!bruises.equalsIgnoreCase("")){
            txtBruises.setVisibility(View.VISIBLE);
            imgBruises.setVisibility(View.VISIBLE);
            txtBruises.setText(bruises);
        }else{
            txtBruises.setVisibility(View.GONE);
            imgBruises.setVisibility(View.GONE);
        }

        if ((bruises.equalsIgnoreCase("") && hemorrhages.equalsIgnoreCase("") && tce.equalsIgnoreCase("") && breakLegs.equalsIgnoreCase("") && breakArms.equalsIgnoreCase("")  && breakHead.equalsIgnoreCase("") && goringHead.equalsIgnoreCase("") && goringBack.equalsIgnoreCase("")  && goringArms.equalsIgnoreCase("") && goringLegs.equalsIgnoreCase(""))){
            txtSymptoms.setVisibility(View.GONE);
        }else{
            txtSymptoms.setVisibility(View.VISIBLE);
        }

        if (!contusionHead.equalsIgnoreCase("")){
            txtContusionHead.setVisibility(View.VISIBLE);
            imgContusionHead.setVisibility(View.VISIBLE);
            txtContusionHead.setText(contusionHead);
        }else{
            txtContusionHead.setVisibility(View.GONE);
            imgContusionHead.setVisibility(View.GONE);
        }
        if (!contusionChest.equalsIgnoreCase("")){
            txtContusionChest.setVisibility(View.VISIBLE);
            imgContusionChest.setVisibility(View.VISIBLE);
            txtContusionChest.setText(contusionChest);
        }else{
            txtContusionChest.setVisibility(View.GONE);
            imgContusionChest.setVisibility(View.GONE);
        }
        if (!contusionBack.equalsIgnoreCase("")){
            txtContusionBack.setVisibility(View.VISIBLE);
            imgContusionBack.setVisibility(View.VISIBLE);
            txtContusionBack.setText(contusionBack);
        }else{
            txtContusionBack.setVisibility(View.GONE);
            imgContusionBack.setVisibility(View.GONE);
        }
        if (!contusionArms.equalsIgnoreCase("")){
            txtContusionArms.setVisibility(View.VISIBLE);
            imgContusionArms.setVisibility(View.VISIBLE);
            txtContusionArms.setText(contusionArms);
        }else{
            txtContusionArms.setVisibility(View.GONE);
            imgContusionArms.setVisibility(View.GONE);
        }
        if (!contusionLegs.equalsIgnoreCase("")){
            txtContusionLegs.setVisibility(View.VISIBLE);
            imgContusionLegs.setVisibility(View.VISIBLE);
            txtContusionLegs.setText(contusionLegs);
        }else{
            txtContusionLegs.setVisibility(View.GONE);
            imgContusionLegs.setVisibility(View.GONE);
        }
        if (!contusionDeformityArms.equalsIgnoreCase("")){
            txtContusionDeformityArms.setVisibility(View.VISIBLE);
            imgDeformitiesArms.setVisibility(View.VISIBLE);
            txtContusionDeformityArms.setText(contusionDeformityArms);
        }else{
            txtContusionDeformityArms.setVisibility(View.GONE);
            imgDeformitiesArms.setVisibility(View.GONE);
        }
        if (!contusionDeformityLegs.equalsIgnoreCase("")){
            txtContusionDeformityLegs.setVisibility(View.VISIBLE);
            imgDeformitiesLegs.setVisibility(View.VISIBLE);
            txtContusionDeformityLegs.setText(contusionDeformityLegs);
        }else{
            txtContusionDeformityLegs.setVisibility(View.GONE);
            imgDeformitiesLegs.setVisibility(View.GONE);
        }
        if (!cuts.equalsIgnoreCase("")){
            txtCuts.setVisibility(View.VISIBLE);
            imgCuts.setVisibility(View.VISIBLE);
            txtCuts.setText(cuts);
        }else{
            txtCuts.setVisibility(View.GONE);
            imgCuts.setVisibility(View.GONE);
        }

        int colorPos = position % colors.length;
        rowView.setBackgroundColor(colors[colorPos]);

        return rowView;
        }
}