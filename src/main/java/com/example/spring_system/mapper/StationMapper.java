package com.example.spring_system.mapper;

import com.example.spring_system.domain.Station;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StationMapper {
    public List<Station> findallStation();
    public int addSattion(Station station);
}
