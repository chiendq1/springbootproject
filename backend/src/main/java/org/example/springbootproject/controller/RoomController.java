package org.example.springbootproject.controller;

import jakarta.validation.Valid;
import org.example.springbootproject.payload.request.CreateRoomRequest;
import org.example.springbootproject.payload.request.RoomTenantsRequest;
import org.example.springbootproject.payload.request.GetRoomListRequest;
import org.example.springbootproject.payload.request.UpdateRoomDetailsRequest;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @GetMapping("/{id}")
    @PreAuthorize("@roomService.hasRoom(#id, authentication.name, false) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> show(@PathVariable int id) {
        try {
            Map<String, Object> roomDetails = roomService.getRoomDetails(id);

            return new ResponseEntity<>(new ApiResponse<>(true, "get room details success", roomDetails, HttpStatus.OK), HttpStatus.OK);
        } catch(Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "get room details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@roomService.hasRoom(#id, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> delete(@PathVariable int id) {
        try {
            roomService.deleteRoom(id);

            return new ResponseEntity<>(new ApiResponse<>(true, "delete room success", null, HttpStatus.OK), HttpStatus.OK);
        } catch(Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "delete room failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('LANDLORD') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid CreateRoomRequest request) {
        Map<String, String> errors = new HashMap<>();
        String roomCodeError = roomService.checkDuplicateField("roomCode", request.getRoomCode(), null);

        if(!roomCodeError.isEmpty()) {
            errors.put("roomCode", roomCodeError);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> roomDetails = roomService.createRoom(request);

        if(roomDetails.get("errors") != null) {
            return new ResponseEntity<>(roomDetails, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "create room success", null, HttpStatus.OK), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    @PreAuthorize("@roomService.hasRoom(#id, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid UpdateRoomDetailsRequest request) {
        try {
            Map<String, String> errors = new HashMap<>();
            String roomCodeError = roomService.checkDuplicateField("roomCode", request.getRoomCode(), id);

            if(!roomCodeError.isEmpty()) {
                errors.put("roomCode", roomCodeError);
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> roomDetails = roomService.updateRoomDetails(id, request);

            if(roomDetails == null) {
                return new ResponseEntity<>(new ApiResponse<>(false, "update room details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(new ApiResponse<>(true, "update room details success", roomDetails, HttpStatus.OK), HttpStatus.OK);
        } catch(Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "update room details failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/tenants")
    @PreAuthorize("@roomService.hasRoom(#id, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<?> addRoomTenants(@PathVariable int id, @RequestBody @Valid RoomTenantsRequest request) {
        Map<String, Object> response = roomService.manageTenants(id, request, true);

        if(response.get("errors") != null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "add room tenants success", response, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/tenants")
    @PreAuthorize("@roomService.hasRoom(#id, authentication.name, true) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoomTenant(@PathVariable int id, @RequestBody @Valid RoomTenantsRequest request) {
        Map<String, Object> response = roomService.manageTenants(id, request, false);

        if(response == null) {
            return new ResponseEntity<>(new ApiResponse<>(false, "delete room tenants failed", null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse<>(true, "delete room tenants success", response, HttpStatus.OK), HttpStatus.OK);
    }

}
