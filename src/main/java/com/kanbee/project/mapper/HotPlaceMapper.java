package com.kanbee.project.mapper;

import com.kanbee.project.model.HotPlace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HotPlaceMapper {
    List<HotPlace> findAll();
    List<HotPlace> findByCategory(@Param("category") String category);
    HotPlace findById(@Param("id") Long id);
    void insert(HotPlace hotPlace);
}
