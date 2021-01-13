package br.com.comeja.comejaempresas.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devspark.robototextview.widget.RobotoTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.comeja.comejaempresas.R;
import br.com.comeja.comejaempresas.interfaces.RecyclerViewOnClickListenerHack;


public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width, height, roundPixels;


    public ProdutoAdapter(Context c, ArrayList<HashMap<String, String>> l) {
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int)(2 * scale + 0.5f);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_produto_card, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        myViewHolder.tv_nomeProduto.setText(mList.get(position).get("nomeProduto"));
        myViewHolder.tv_idProduto.setText(mList.get(position).get("idProduto"));
        myViewHolder.tv_statusProduto.setText(mList.get(position).get("statusProduto"));


        try {
            YoYo.with(Techniques.Bounce)
                    .duration(700)
                    .playOn(myViewHolder.itemView);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(HashMap<String, String> c, int position) {
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RobotoTextView tv_nomeProduto, tv_idProduto, tv_statusProduto;


        public MyViewHolder(View itemView) {
            super(itemView);


            tv_nomeProduto = (RobotoTextView) itemView.findViewById(R.id.tv_nomeProduto);
            tv_idProduto = (RobotoTextView) itemView.findViewById(R.id.tv_idProduto);
            tv_statusProduto = (RobotoTextView) itemView.findViewById(R.id.tv_statusProduto);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
