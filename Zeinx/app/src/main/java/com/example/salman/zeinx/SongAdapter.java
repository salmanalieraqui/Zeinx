package com.example.salman.zeinx;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Salman on 30-08-2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    ArrayList<SongInfo> _songs;
    Context context;
    OnitemClickListener onitemClickListener;
    SongAdapter(Context context,ArrayList<SongInfo>_songs){
        this.context=context;
        this._songs=_songs;
    }
    public  interface OnitemClickListener{
        void onItemClick(Button b, View v, SongInfo obj, int position );
    }
    public  void setOnitemClickListener(OnitemClickListener onitemClickListener){
        this.onitemClickListener=onitemClickListener;
    }
    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(context).inflate(R.layout.row_song,parent,false);
        return new SongHolder(myView);
    }

    @Override
    public void onBindViewHolder(final SongHolder holder, final int position) {

        final SongInfo c=_songs.get(position);
        holder.songName.setText(c.songName);
        holder.artistName.setText(c.artistName);
        holder.btnAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(onitemClickListener !=null){
                    onitemClickListener.onItemClick(holder.btnAction,view,c,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView songName,artistName;
        Button btnAction;
        public SongHolder(View itemView) {
            super(itemView);
            songName=(TextView)itemView.findViewById(R.id.TvSongName);
            artistName=(TextView)itemView.findViewById(R.id.TvArtistName);
            btnAction=(Button)itemView.findViewById(R.id.btnAction);

        }
    }
}
