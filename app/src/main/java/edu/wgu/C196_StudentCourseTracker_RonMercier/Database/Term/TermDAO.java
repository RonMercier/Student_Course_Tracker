package edu.wgu.C196_StudentCourseTracker_RonMercier.Database.Term;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(TermEntity termEntity);

    @Update
    void updateTerm(TermEntity termEntity);

    @Delete
    void deleteTerm(TermEntity termEntity);

    @Query("SELECT * FROM terms WHERE term_id = :termID")
    TermEntity getTermByID(int termID);

    @Query("SELECT * FROM terms ORDER BY term_id ASC")
    LiveData<List<TermEntity>> getAllTerms();

    @Query("DELETE FROM terms")
    void deleteAllTerms();

    @Query("SELECT COUNT(*) FROM terms")
    int getTermCount();

}
