package net.kdilla.wetharium.fragments;

import net.kdilla.wetharium.DB.WeatherNote;

import java.util.List;

/**
 * Created by avetc on 12.12.2017.
 */

public interface LatShownInterface{
    void getElements(List<WeatherNote> elements);
}