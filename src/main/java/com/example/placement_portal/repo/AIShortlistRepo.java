package com.example.placement_portal.repo;

import com.example.placement_portal.entity.AIShortlist;
import com.example.placement_portal.entity.Job;
import com.example.placement_portal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;;
import java.util.List;

@Repository
public interface AIShortlistRepo extends JpaRepository<AIShortlist, Long> {

    // Find by job
    List<AIShortlist> findByJob(Job job);
    List<AIShortlist> findByJobId(Long jobId);

    // Find by student
    List<AIShortlist> findByStudent(Student student);
    List<AIShortlist> findByStudentId(Long studentId);

    // Find by job ordered by rank
    List<AIShortlist> findByJobOrderByScoreDesc(Job job);

    // Find top N ranked candidates for a job
    Page<AIShortlist> findByJobOrderByRankAsc(Job job, Pageable pageable);

    // Find by score range
    List<AIShortlist> findByScoreBetween(Double minScore, Double maxScore);

    // Find candidates above a certain score
    List<AIShortlist> findByScoreGreaterThanEqual(Double minScore);

    // Delete by job
    @Modifying
    @Query("DELETE FROM AIShortlist a WHERE a.job.id = :jobId")
    void deleteByJobId(@Param("jobId") Long jobId);

    // Count shortlisted candidates for a job
    @Query("SELECT COUNT(a) FROM AIShortlist a WHERE a.job = :job")
    Long countByJob(@Param("job") Job job);

    // Get average score for a job
    @Query("SELECT AVG(a.score) FROM AIShortlist a WHERE a.job = :job")
    Double getAverageScoreForJob(@Param("job") Job job);
}