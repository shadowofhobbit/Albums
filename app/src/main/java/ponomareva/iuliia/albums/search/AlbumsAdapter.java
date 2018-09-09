package ponomareva.iuliia.albums;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    private List<Album> albums = new ArrayList<>();
    private AlbumClickListener listener;

    AlbumsAdapter(AlbumClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        final AlbumViewHolder holder = new AlbumViewHolder(view);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(albums.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.textView.setText(album.getCollectionName());
        holder.imageView.setContentDescription(album.getCollectionName());
        Picasso.get().load(album.getArtworkUrl100()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    void setData(Collection<Album> albums) {
        this.albums.clear();
        this.albums.addAll(albums);
        Collections.sort(this.albums, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                return Collator.getInstance().compare(o1.getCollectionName(), o2.getCollectionName());
            }
        });
        notifyDataSetChanged();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) ImageView imageView;
        @BindView(R.id.name) TextView textView;
        AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

interface AlbumClickListener {
    void onClick(Album album);
}

