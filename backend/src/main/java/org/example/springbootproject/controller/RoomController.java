package org.example.springbootproject.controller;

import org.example.springbootproject.payload.request.GetRoomListRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController extends BaseController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private AuthService authService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Map<String, Object>>> index(
            @RequestParam(value = "priceRange", required = false) float[] priceRange,
            @RequestParam(value = "capacity", required = false) int[] capacity,
            @RequestParam(value = "area", required = false) float[] area,
            @RequestParam(value = "searchValue", required = false) String searchValue,
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "status", required = false) int[] status
    ) {
        try {
            GetRoomListRequest request = new GetRoomListRequest();
            request.setArea(area);
            request.setPriceRange(priceRange);
            request.setCapacity(capacity);
            request.setSearchValue(searchValue);
            request.setPageNo(pageNo);
            request.setStatus(status);
            String currentUserName = authService.getCurrentUser().getUsername();
            Map<String, Object> response = roomService.getListRooms(request, currentUserName);
            return new ResponseEntity<>(new ApiResponse<>(true, "get rooms success", response, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "get rooms failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
