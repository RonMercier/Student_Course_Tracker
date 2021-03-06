package edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Course;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface CourseDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(CourseEntity courseEntity);

    @Update
    void updateCourse(CourseEntity courseEntity);

    @Query("DELETE FROM courses")
    void deleteAllCourses();

    @Query("SELECT * FROM COURSES")
    LiveData<List<CourseEntity> > getAllCourses();

    @Delete
    void deleteCourse(CourseEntity courseEntity);

    @Query("SELECT * FROM COURSES WHERE course_id = :courseID")
    CourseEntity getCourseByID(int courseID);

    @Query("SELECT COUNT(*) FROM COURSES")
    int getCourseCount();

    @Query("SELECT * from courses where term_id= :term_id")
    LiveData<List<CourseEntity>> getCoursesByTermId(int term_id);

}
