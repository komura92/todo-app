package com.example.todoapp.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todoapp.domain.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true, value = "select * " +
            "from task " +
            "where id = :id " +
            "  and user_id = :userId " +
            "  and status = :status " +
            "order by id;")
    Optional<Task> getMyTask(@Param("id") Long id,
                             @Param("userId") String actualUserId,
                             @Param("status") String status);

    @Query(nativeQuery = true, value = "select * " +
            "from task " +
            "where user_id = :userId " +
            "  and status = :status " +
            "order by priority desc, id " +
            "limit :quantity offset :startIndex ;")
    List<Task> getMyTasksByStatus(@Param("userId") String actualUserId,
                                  @Param("status") String status,
                                  @Param("startIndex") Long startIndex,
                                  @Param("quantity") int quantity);
}
