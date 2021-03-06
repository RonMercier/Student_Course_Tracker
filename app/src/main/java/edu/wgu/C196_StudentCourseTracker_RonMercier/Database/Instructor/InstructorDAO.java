package edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Instructor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface InstructorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InstructorEntity instructorEntity);

    @Update
    void updateInstructor(InstructorEntity instructorEntity);



    @Query("DELETE FROM instructors")
    void deleteAllInstructors();

    @Query("SELECT * FROM instructors order by instructor_name")
    LiveData<List<InstructorEntity>> getAllInstructors();

    @Query("SELECT * FROM instructors where instructor_id = :instructorID")
    InstructorEntity getInstructorByID(int instructorID);

    @Delete
    void delete(InstructorEntity instructorEntity);

}
