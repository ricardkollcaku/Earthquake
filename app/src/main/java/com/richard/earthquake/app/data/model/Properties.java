package com.richard.earthquake.app.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Properties {
    private Double mag;
    private String place;
    private long time;
    private long updated;
    private Integer tz;
    private String url;
    private String detail;
    private String status;
    private Integer tsunami;
    private Integer sig;
    private String net;
    private String code;
    private String ids;
    private String sources;
    private String types;
    private Integer nst;
    private Double dmin;
    private Double rms;
    private Double gap;
    private String magType;
    private String type;
    private String title;
}
