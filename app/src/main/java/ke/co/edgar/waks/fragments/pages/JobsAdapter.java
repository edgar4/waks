package ke.co.edgar.waks.fragments.pages;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ke.co.edgar.waks.R;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.MyViewHolder> {

    private List<Job> JobsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, post_time, description, salaryAmount,location,type;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.job_title);
            description = (TextView) view.findViewById(R.id.description);
            post_time = (TextView) view.findViewById(R.id.post_time);
            salaryAmount = (TextView) view.findViewById(R.id.salary_amount);
            location = (TextView) view.findViewById(R.id.location);

            type  = (TextView)  view.findViewById(R.id.type);
        }
    }


    public JobsAdapter(List<Job> JobsList) {
        this.JobsList = JobsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Job job = JobsList.get(position);
        holder.title.setText(job.getTitle());
        holder.description.setText(job.getDescription());
        holder.post_time.setText(job.getPostTime());
        holder.salaryAmount.setText(job.getSalaryAmount());
        holder.location.setText(job.getLocation());
    }

    @Override
    public int getItemCount() {
        return JobsList.size();
    }
}
