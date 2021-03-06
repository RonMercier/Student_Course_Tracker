package edu.wgu.C196_StudentCourseTracker_RonMercier.UI.CourseActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Course.CourseEntity;
import edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Course.CourseViewModel;
import edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Instructor.InstructorEntity;
import edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Instructor.InstructorViewModel;
import edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Term.TermEntity;
import edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Term.TermViewModel;
import edu.wgu.C196_StudentCourseTracker_RonMercier.R;

public class CourseAddEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TermViewModel termViewModel;
    InstructorViewModel instructorViewModel;
    CourseViewModel courseViewModel;
    public Integer courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        instructorViewModel = new ViewModelProvider(this).get(InstructorViewModel.class);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_edit);

        EditText courseNameEditText = findViewById(R.id.courseNameEditText);
        EditText courseStartDateEditText = findViewById(R.id.courseStartDateEditText);
        EditText courseEndDateEditText = findViewById(R.id.courseEndDateEditText);
        EditText courseNoteMultiLine = findViewById(R.id.courseNoteMultiLine);
        Button courseSaveBtn = findViewById(R.id.courseSaveBtn);


//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
        Spinner courseTermSpinner = findViewById(R.id.courseTermSpinner);
        List<TermEntity> terms = new ArrayList<>();

        ArrayAdapter<TermEntity> termSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, terms);

        termSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        termViewModel.getAllTerms().observe(this, termSpinnerAdapter::addAll);

        termSpinnerAdapter.notifyDataSetChanged();

        courseTermSpinner.setAdapter(termSpinnerAdapter);
        courseTermSpinner.setOnItemSelectedListener(this);

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
        Spinner courseInstructorSpinner = findViewById(R.id.courseInstructorSpinner);
        List<InstructorEntity> instructors = new ArrayList<>();

        ArrayAdapter<InstructorEntity> instructorSpinnerAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,instructors);
        instructorSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        instructorViewModel.getAllInstructors().observe(this, instructorSpinnerAdapter::addAll);

        instructorSpinnerAdapter.notifyDataSetChanged();
        courseInstructorSpinner.setAdapter(instructorSpinnerAdapter);
        courseInstructorSpinner.setOnItemSelectedListener(this);



//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

        Spinner courseStatusSpinner = findViewById(R.id.courseStatusSpinner);

        ArrayList<String> statuses = new ArrayList<>();
        statuses.add("In Progress");
        statuses.add("Completed");
        statuses.add("Dropped");
        statuses.add("Plan To Take");

        ArrayAdapter<String> statusSpinnerAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,statuses);
        statusSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        courseStatusSpinner.setAdapter(statusSpinnerAdapter);
        courseStatusSpinner.setOnItemSelectedListener(this);

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------


        courseInstructorSpinner.setSelection(getSpinnerIndex(courseInstructorSpinner,intent.getStringExtra("course_status")));




        if (intent.hasExtra("courseID")){
            setTitle("Edit Course");
            setCourseID(intent.getIntExtra("courseID",0));

            instructorViewModel = new ViewModelProvider(this).get(InstructorViewModel.class);
            CourseEntity courseEntity = courseViewModel.getCourseById(courseID);



            courseNameEditText.setText(courseEntity.getCourse_title());
            courseStartDateEditText.setText(courseEntity.getCourse_start());
            courseEndDateEditText.setText(courseEntity.getCourse_end());
            courseNoteMultiLine.setText(courseEntity.getCourse_note());


            courseStatusSpinner.setSelection(getSpinnerIndex(courseStatusSpinner,courseEntity.getCourse_status()));
            String instructorName = intent.getStringExtra("instructorName");
            courseInstructorSpinner.setSelection(getSpinnerIndex(courseInstructorSpinner,instructorName));
            courseTermSpinner.setSelection(courseEntity.getTerm_id());



            courseSaveBtn.setOnClickListener(v -> {
                String status = courseStatusSpinner.getSelectedItem().toString();
                int termID =((TermEntity)( courseTermSpinner.getSelectedItem())).getTerm_id();
                int instructorID = ((InstructorEntity)(courseInstructorSpinner.getSelectedItem())).getId();

                courseEntity.setCourse_title(courseNameEditText.getText().toString());
                courseEntity.setCourse_start(courseStartDateEditText.getText().toString());
                courseEntity.setCourse_end(courseEndDateEditText.getText().toString());
                courseEntity.setCourse_note(courseNoteMultiLine.getText().toString());
                courseEntity.setInstructor_id(instructorID);
                courseEntity.setTerm_id(termID);
                courseEntity.setCourse_status(status);
                courseViewModel.updateCourse(courseEntity);
                Intent updateIntent = new Intent(CourseAddEditActivity.this,CourseDetailActivity.class);
                updateIntent.putExtra("courseID", courseID);
                Toast.makeText(this,"Course "+courseEntity.getCourse_title()+" saved!",Toast.LENGTH_SHORT).show();
                startActivity(updateIntent);
            });


        }
        else{
            setTitle("Add Course");

            courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

            courseSaveBtn.setOnClickListener(v -> {
                String status = courseStatusSpinner.getSelectedItem().toString();
                int termID =((TermEntity)( courseTermSpinner.getSelectedItem())).getTerm_id();
                int instructorID = ((InstructorEntity)(courseInstructorSpinner.getSelectedItem())).getId();

                CourseEntity course = new CourseEntity(
                        courseNameEditText.getText().toString(),
                        courseStartDateEditText.getText().toString(),
                        courseEndDateEditText.getText().toString(),
                        status,
                        courseNoteMultiLine.getText().toString(),
                        termID,
                        instructorID
                );
                courseViewModel.insertCourse(course);
                Intent updateIntent = new Intent(CourseAddEditActivity.this,CourseActivity.class);
                startActivity(updateIntent);
            });

        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_36);


    }

    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().trim().equals(myString.trim())) {
                index = i;
            }
        }
        return index;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("Nothing Selected!");
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if (courseID == null){
            intent = new Intent(CourseAddEditActivity.this, CourseActivity.class);
        }else {
            intent = new Intent(CourseAddEditActivity.this, CourseDetailActivity.class);
            intent.putExtra("courseID",courseID);
        }
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }



//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

    //    Lifecycle Logs
    private final String TAG = "Lifecycle";
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");




    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
}