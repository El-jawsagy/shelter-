package com.life.shelter.people.homeless;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


/**
 * Created by AHMED MAGDY on 9/15/2018.
 */

public class TrampHomeAdapter extends ArrayAdapter<HomeFirebaseClass> {

    private Activity context;
    private List<HomeFirebaseClass> trampList;
    private String a1, a2;


    public TrampHomeAdapter(Activity context, List<HomeFirebaseClass> trampList) {
        super(context, R.layout.list_layout_home, trampList);
        this.context = context;
        this.trampList = trampList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.list_layout_home, null, true);

        final TextView atrampname = (TextView) listViewItem.findViewById(R.id.tramp_name);
        final TextView atrampaddress = (TextView) listViewItem.findViewById(R.id.tramp_address);
        final TextView atrampcity = (TextView) listViewItem.findViewById(R.id.tramp_city);


        int b ;
        final CheckBox tasken =(CheckBox)listViewItem.findViewById(R.id.tasken);
        boolean checktasken =tasken.isChecked();

        if (checktasken =true){
            b=1;

        }
        else {
            b=2;
        }






        final ImageView atrampphoto = (ImageView) listViewItem.findViewById(R.id.tramp_photo);
        final ImageView auserphoto = (ImageView) listViewItem.findViewById(R.id.user_name_logo_list);



        final TextView ausername = (TextView) listViewItem.findViewById(R.id.user_name_list);
        final TextView adate = (TextView) listViewItem.findViewById(R.id.date_list);
        ausername.equals(null);
        final ImageView afacelogo = (ImageView) listViewItem.findViewById(R.id.face_logo);
        final ImageView atweeterlogo = (ImageView) listViewItem.findViewById(R.id.tweeter_logo);
        final ImageView aemaillogo = (ImageView) listViewItem.findViewById(R.id.email_logo);
        final ImageView adonatelogo = (ImageView) listViewItem.findViewById(R.id.donate_logo);

        HomeFirebaseClass hometramp = trampList.get(position);
        //asize = trampList.size();


        atrampname.setText(hometramp.getcName());
        atrampaddress.setText(hometramp.getcAddress());
        atrampcity.setText(hometramp.getcCity());

        a1=hometramp.getcUri();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(16));
        //requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

        Glide.with(context)
                .load(a1)
                .apply(requestOptions)
                .into(atrampphoto);



        adate.setText(hometramp.getPdate());

     ////////////////////////////////
        a2=hometramp.getUserUri();
       /** Glide.with(context)
                .load(a2)
                .into(auserphoto);

        ausername.setText(hometramp.getUsername());**/

          if(a2 != null){
         Glide.with(context)
         .load(a2)
                 .apply(RequestOptions.circleCropTransform())
         .into(auserphoto);
         }else {
         Glide.with(context)
         .load("https://firebasestorage.googleapis.com/v0/b/shelter-87aaa.appspot.com/o/user.png?alt=media&token=0a6b51c3-f1ec-4fea-a0eb-a7eaa45875d4")
                 .apply(RequestOptions.circleCropTransform())
                 .into(auserphoto);         }

         if(hometramp.getUsername() != null){
         ausername.setText(hometramp.getUsername());
         }else {

         ausername.setText("Unknown name");
            // ausername.equals(null);
         }


return listViewItem;
    }
}

