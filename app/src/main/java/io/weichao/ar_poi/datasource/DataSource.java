package io.weichao.ar_poi.datasource;

import java.util.List;

import io.weichao.ar_poi.domain.Marker;

public abstract class DataSource {
    public abstract List<Marker> getMarkers();
}
