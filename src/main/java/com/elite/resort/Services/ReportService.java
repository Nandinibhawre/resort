package com.elite.resort.Services;

import com.elite.resort.DTO.ReportStatsDTO;
import com.elite.resort.Repository.RoomRepo;
import com.elite.resort.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserRepo userRepository;
    private final RoomRepo roomRepository;

    public ReportStatsDTO getDashboardStats() {

        long totalUsers = userRepository.count();
        long totalRooms = roomRepository.count();
        long activeRooms = roomRepository.countByAvailableTrue();
        long inactiveRooms = roomRepository.countByAvailableFalse();

        return new ReportStatsDTO(
                totalUsers,
                totalRooms,
                activeRooms,
                inactiveRooms
        );
    }
}
