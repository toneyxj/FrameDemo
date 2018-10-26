package com.xj.mainframe.adapter;

import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.utils.GlideUtils;

import java.util.HashMap;
import java.util.Map;

public class SmartViewHolder extends RecyclerView.ViewHolder  {

    private final AdapterView.OnItemClickListener mListener;
    private int mPosition = -1;
    private Map<Integer,View> views=new HashMap<>();

    public SmartViewHolder(View itemView, AdapterView.OnItemClickListener mListener) {
        super(itemView);
        this.mListener = mListener;
        itemView.setOnClickListener(clickListener);

        /*
         * 设置水波纹背景
         */
        if (itemView.getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = itemView.getContext().getTheme();
            int top = itemView.getPaddingTop();
            int bottom = itemView.getPaddingBottom();
            int left = itemView.getPaddingLeft();
            int right = itemView.getPaddingRight();
            if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                itemView.setBackgroundResource(typedValue.resourceId);
            }
            itemView.setPadding(left, top, right, bottom);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }


    private XJOnClickListener clickListener=new XJOnClickListener() {
        @Override
        public void onclickView(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if(position >= 0){
                    mListener.onItemClick(null, view, position, getItemId());
                } else if (mPosition > -1) {
                    mListener.onItemClick(null, view, mPosition, getItemId());
                }
            }
        }
    };
    private View findViewById(int id) {
        if (id==0)return itemView;
        View view=null;
        try {
            view=views.get(id);
            if (null==view){
                 view=itemView.findViewById(id);
            }
        }catch ( Exception e){
            e.printStackTrace();
            view=itemView.findViewById(id);
        }
        return view;
    }

    public SmartViewHolder text(int id, CharSequence sequence) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(sequence);
        }
        return this;
    }

    public SmartViewHolder text(int id,@StringRes int stringRes) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(stringRes);
        }
        return this;
    }

    public SmartViewHolder textColorId(int id, int colorId) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), colorId));
        }
        return this;
    }

    public SmartViewHolder image(int id, int imageId) {
        View view = findViewById(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(imageId);
        }
        return this;
    }
    public SmartViewHolder netImage(int id,String netImage){
        View view = findViewById(id);
        if (view instanceof ImageView) {
            GlideUtils.getInstance().loadImage(view.getContext(),((ImageView) view),netImage);
        }
        return this;
    }
}