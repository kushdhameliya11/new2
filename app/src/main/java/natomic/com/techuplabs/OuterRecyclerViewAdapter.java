package natomic.com.techuplabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OuterRecyclerViewAdapter extends RecyclerView.Adapter<OuterRecyclerViewAdapter.OuterViewHolder> {

    private Context mContext;
    private List<OuterItem> mOuterItemsList;
    private OuterRecyclerViewAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int imageResourceId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public OuterRecyclerViewAdapter(Context context, List<OuterItem> outerItemsList) {
        mContext = context;
        mOuterItemsList = outerItemsList;
    }

    @NonNull
    @Override
    public OuterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.outer_recyclerview_item, parent, false);
        return new OuterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterViewHolder holder, int position) {
        OuterItem outerItem = mOuterItemsList.get(position);
        holder.textView.setText(outerItem.getText());

        InnerRecyclerViewAdapter innerAdapter = new InnerRecyclerViewAdapter(mContext, outerItem.getInnerItems());
        innerAdapter.setOnItemClickListener(imageResourceId -> {
            if (mListener != null) {
                mListener.onItemClick(imageResourceId);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        holder.innerRecyclerView.setLayoutManager(gridLayoutManager);
        holder.innerRecyclerView.setHasFixedSize(true);
        holder.innerRecyclerView.setAdapter(innerAdapter);

        int spacing = mContext.getResources().getDimensionPixelSize(R.dimen.item_spacing);

        // Remove all existing item decorations to prevent duplicate decorations
        while (holder.innerRecyclerView.getItemDecorationCount() > 0) {
            holder.innerRecyclerView.removeItemDecorationAt(0);
        }

        holder.innerRecyclerView.addItemDecoration(new GridItemdecoration(4, spacing, true));
    }

    @Override
    public int getItemCount() {
        return mOuterItemsList.size();
    }

    public static class OuterViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView innerRecyclerView;

        public OuterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.outer_text_view);
            innerRecyclerView = itemView.findViewById(R.id.inner_recycler_view);
        }
    }
}
