package edu.wgu.C196_StudentCourseTracker_RonMercier.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Course.CourseEntity;
import edu.wgu.C196_StudentCourseTracker_RonMercier.R;
import edu.wgu.C196_StudentCourseTracker_RonMercier.UI.CourseActivities.CourseDetailActivity;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private List<CourseEntity> courses = new ArrayList<>();

    public CourseAdapter(Context context) {
        inflater=LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (courses!=null){
            final CourseEntity current = courses.get(position);
            holder.courseTitle.setText(current.getCourse_title());
            holder.courseStart.setText(current.getCourse_start());
            holder.courseEnd.setText(current.getCourse_end());
        }
    }

    @Override
    public int getItemCount() {
        if(courses!=null){
            return courses.size();
        }
        else return 0;
    }

    public List<CourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseEntity> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public CourseEntity getCourseAtPosition(int position) {
        return courses.get(position);
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseTitle;
        private final TextView courseStart;
        private final TextView courseEnd;


        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle=itemView.findViewById(R.id.instructorName);
            courseStart=itemView.findViewById(R.id.instructorPhoneListItemTextView);
            courseEnd=itemView.findViewById(R.id.instructorEmailListItemTextView);
            itemView.setOnClickListener(v->{
                int position = getAdapterPosition();
                final CourseEntity current = courses.get(position);
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("courseID", current.getCourse_id());

                context.startActivity(intent);
            });

        }
    }
}
