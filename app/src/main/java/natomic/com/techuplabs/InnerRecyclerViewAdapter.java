package natomic.com.techuplabs;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InnerRecyclerViewAdapter extends RecyclerView.Adapter<InnerRecyclerViewAdapter.InnerViewHolder> {

    private Context mContext;
    private List<Integer> mImagesList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int imageResourceId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public InnerRecyclerViewAdapter(Context context, List<Integer> imagesList) {
        mContext = context;
        mImagesList = imagesList;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.inner_recyclerview_item, parent, false);
        return new InnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        int imageResource = mImagesList.get(position);

        holder.imageView.setClipToOutline(true);
        holder.imageView.setImageResource(imageResource);
        // Set rounded corners programmatically
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(8); // Adjust the radius as needed
        holder.imageView.setBackground(drawable);

        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onItemClick(imageResource);
            }
        });

        // Ensure consistent layout parameters if needed
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.imageView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public static class InnerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.inner_image_view);
        }
    }
}
