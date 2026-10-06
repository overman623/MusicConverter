package com.happyhouse.music.converter.help;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happyhouse.music.converter.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LicenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static final String TAG = LicenseListAdapter.class.getSimpleName();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textCopyRight;
        TextView textLink;
        TextView textContent;

        MyViewHolder(View view){
            super(view);
            textTitle = view.findViewById(R.id.text_title);
            textCopyRight = view.findViewById(R.id.text_copyright);
            textLink = view.findViewById(R.id.text_link);
            textContent = view.findViewById(R.id.text_content);
        }
    }

    private ArrayList<LicenseItem> licenseItems;

    LicenseListAdapter(ArrayList<LicenseItem> licenseItems){
        this.licenseItems = licenseItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_license, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.textTitle.setText(licenseItems.get(position).getTitle());
        myViewHolder.textCopyRight.setText(licenseItems.get(position).getCopyRight());
        myViewHolder.textLink.setText(licenseItems.get(position).getLink());
        myViewHolder.textContent.setText(licenseItems.get(position).getContent());

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        String linkSite = myViewHolder.textLink.getText().toString();

        String linkLicenseSite = myViewHolder.textContent.getText().toString();
        int indexLicenseSite = 0;
        if(linkLicenseSite.contains("https://")) indexLicenseSite = linkLicenseSite.indexOf("https://");
        else if(linkLicenseSite.contains("http://")) indexLicenseSite = linkLicenseSite.indexOf("http://");
        linkLicenseSite = linkLicenseSite.substring(indexLicenseSite);
        Pattern patternSite = Pattern.compile(linkSite);
        Pattern patternLicenseSite = Pattern.compile(linkLicenseSite);
        Linkify.addLinks(myViewHolder.textLink, patternSite, linkSite, null, mTransform);
        Linkify.addLinks(myViewHolder.textContent, patternLicenseSite, linkLicenseSite,null, mTransform);

    }

    public void removeItem(int position){
        licenseItems.remove(position);
    }

    @Override
    public int getItemCount() {
        return licenseItems.size();
    }


}