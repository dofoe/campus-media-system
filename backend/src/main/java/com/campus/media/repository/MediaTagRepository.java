package com.campus.media.repository;

import com.campus.media.entity.MediaTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaTagRepository extends JpaRepository<MediaTag, Long> {
    
    List<MediaTag> findByMediaId(Long mediaId);
    
    List<MediaTag> findByTagId(Long tagId);
    
    void deleteByMediaId(Long mediaId);
    
    boolean existsByMediaIdAndTagId(Long mediaId, Long tagId);
}
