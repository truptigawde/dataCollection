//package org.cabi.pdc.adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import org.cabi.pdc.R;
//import org.cabi.pdc.SqliteDb.database.model.SqliteDb.model.DB_Form;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//public class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.MyViewHolder> {
//
//    private Context context;
//    private List<DB_Form> formsList;
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView plantDoctor;
//        public TextView clinicCode;
//        public TextView formDate;
//
//        public MyViewHolder(View view) {
//            super(view);
//            plantDoctor = view.findViewById(R.id.txtPD);
//            clinicCode = view.findViewById(R.id.txtCC);
//            formDate = view.findViewById(R.id.txtDate);
//        }
//    }
//
//    public FormsAdapter(Context context, List<DB_Form> formsList) {
//        this.context = context;
//        this.formsList = formsList;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_list_item, parent, false);
//
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(FormsAdapter.MyViewHolder holder, int position) {
//
//        DB_Form form = formsList.get(position);
//
//        holder.plantDoctor.setText(form.getPlantDoctor());
//        holder.clinicCode.setText(form.getClinicCode());
//        holder.formDate.setText(formatDate(form.getFormDate()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return formsList.size();
//    }
//
//    private String formatDate(String dateStr) {
//        try {
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = fmt.parse(dateStr);
//            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
//            return fmtOut.format(date);
//        } catch (Exception e) {
//
//        }
//        return null;
//    }
//}
