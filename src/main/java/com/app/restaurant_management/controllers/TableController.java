package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.config.ApiResponse;
import com.app.restaurant_management.models.Tables;
import com.app.restaurant_management.services.impl.TableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstants.TABLE)
public class TableController {

    private final TableServiceImpl tableServiceImpl;

    @Autowired
    public TableController(TableServiceImpl tableServiceImpl) {
        this.tableServiceImpl = tableServiceImpl;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Tables>>> getAll() throws CustomException {
        ApiResponse<List<Tables>> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, tableServiceImpl.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<ApiResponse<Tables>> getById(@PathVariable Long tableId) {
        ApiResponse<Tables> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, tableServiceImpl.getById(tableId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Tables>> create(@RequestBody Tables table) {
        ApiResponse<Tables> response = new ApiResponse<>(String.valueOf(HttpStatus.CREATED.value()), MessageResponseConstants.CREATE_RESPONSE, tableServiceImpl.create(table));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Tables>> update(@RequestParam("tableId") Long tableId, @RequestBody Tables newTable) {
        ApiResponse<Tables> response = new ApiResponse<>("200", MessageResponseConstants.UPDATE_RESPONSE, tableServiceImpl.update(tableId, newTable));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ApiResponse<Boolean>> delete(@RequestParam("tableId") Long tableId) throws CustomException {
        ApiResponse<Boolean> response = new ApiResponse<>("200", MessageResponseConstants.HEARD_DELETE_RESPONSE, tableServiceImpl.delete(tableId));
        return ResponseEntity.ok(response);
    }
}
