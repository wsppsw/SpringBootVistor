package com.example.spring_system.service.Imp;

import com.example.spring_system.domain.Station;
import com.example.spring_system.mapper.StationMapper;
import com.example.spring_system.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationServiceImp implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public List<Station> findallStation() {
        return stationMapper.findallStation();
    }

    @Override
    public int addSattion(Station station) {
        return stationMapper.addSattion(station);
    }
}
