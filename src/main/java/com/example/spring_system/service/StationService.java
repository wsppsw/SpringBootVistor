package com.example.spring_system.service;

import com.example.spring_system.domain.Station;

import java.util.List;

public interface StationService {
    public List<Station> findallStation();
    public int addSattion(Station station);
}
