package com.craftelix.filestorage.repository;

import com.craftelix.filestorage.entity.DataInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DataInfoRepository extends JpaRepository<DataInfo, Long> {

    List<DataInfo> findByParentPathAndUserId(String path, Long userId, Sort sort);

    List<DataInfo> findByNameIgnoreCaseContainingAndUserId(String name, Long userId, Sort sort);

    boolean existsByPathAndIsFolderAndUserId(String path, boolean isFolder, Long userId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE DataInfo d
            SET d.name = :newName,
                d.path = REPLACE(d.path, :oldPath, :newPath)
            WHERE d.path = :oldPath and d.user.id = :userId
            """)
    void updateByPath(String oldPath, String newPath, String newName, Long userId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE DataInfo d
            SET d.parentPath = REPLACE(d.parentPath, :oldPath, :newPath),
                d.path = REPLACE(d.path, :oldPath, :newPath)
            WHERE d.parentPath LIKE CONCAT(:oldPath, '%') and d.user.id = :userId
            """)
    void updateByParentPathPrefix(String oldPath, String newPath, Long userId);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM DataInfo d
            WHERE d.path LIKE CONCAT(:path, '%') AND d.user.id = :userId
            """)
    void deleteByPathPrefix(String path, Long userId);
}