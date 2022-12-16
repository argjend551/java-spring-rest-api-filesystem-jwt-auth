package com.example.FileApp.repositories;

import com.example.FileApp.data.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

    Optional<File> findById(String id);

    @Query(value = "SELECT * FROM files WHERE user_id = ?1", nativeQuery = true)
    List<File> findAllById(String userId);

    @Modifying
    @Query(value = "DELETE FROM files WHERE file_id = ?1", nativeQuery = true)
    void deleteById(String fileId);
}
