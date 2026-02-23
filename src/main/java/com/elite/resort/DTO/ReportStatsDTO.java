package com.elite.resort.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportStatsDTO {

    private long totalUsers;
    private long totalRooms;
    private long activeRooms;
    private long inactiveRooms;

}
