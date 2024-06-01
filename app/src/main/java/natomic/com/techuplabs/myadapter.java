package natomic.com.techuplabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder> {
    ArrayList<model> dataholder;
    private Context context;

    public myadapter(ArrayList<model> dataholder, Context context) {
        this.dataholder = dataholder;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        model currentData = dataholder.get(position);
        holder.dname.setMaxLines(2);
        holder.createdDate.setMaxLines(2);
        holder.dname.setEllipsize(TextUtils.TruncateAt.END);
        holder.createdDate.setEllipsize(TextUtils.TruncateAt.END);
        holder.dname.setText(currentData.getName());
        holder.createdDate.setText(currentData.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public void setData(ArrayList<model> newData) {
        this.dataholder = newData;
    }


    class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dname;
        TextView createdDate;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            dname = itemView.findViewById(R.id.displayname);
            createdDate = itemView.findViewById(R.id.createdDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            String createdAt = dataholder.get(pos).getCreatedAt();

            // Parse the createdAt string to get the month name
            String[] parts = createdAt.split("-"); // Assuming createdAt format is "dd-MM-yyyy"
            int month = Integer.parseInt(parts[1]) - 1; // Month starts from 0 in Calendar class
            String[] monthNames = new DateFormatSymbols().getShortMonths();
            String monthName = monthNames[month];

            // Prepare the intent with the month name
            Intent intent = new Intent(context, ViewNoteActivity.class);
            intent.putExtra("name", dataholder.get(pos).getName());
            intent.putExtra("createdDate", parts[0]+" " +monthName + " " +  ", " + parts[2]); // Format: MonthName day, year
            context.startActivity(intent);

            ((Activity) context).overridePendingTransition(R.anim.slidein, R.anim.static_animation);
        }
    }
}