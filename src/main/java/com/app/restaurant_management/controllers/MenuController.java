package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.config.ApiResponse;
import com.app.restaurant_management.models.Menu;
import com.app.restaurant_management.services.MenuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(PathConstants.MENU)
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Menu>>> getAllMenus(@RequestParam(required = false) String menuName) throws CustomException {
        List<Menu> menus;

        if (menuName != null && !menuName.isEmpty()){
            menus = menuService.getMenuByName(menuName);
        }else {
            menus = menuService.getAllMenus();
        }

        ApiResponse<List<Menu>> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, menus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<Menu>> getMenuById(@PathVariable Long menuId) {
        Menu menu = menuService.getMenuById(menuId);
        ApiResponse<Menu> response = new ApiResponse<>("200", MessageResponseConstants.GET_RESPONSE, menu);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Menu>> addMenu(@Valid @RequestBody Menu menu) throws JsonProcessingException {
        ApiResponse<Menu> response = new ApiResponse<>(String.valueOf(HttpStatus.CREATED.value()), MessageResponseConstants.CREATE_RESPONSE, menuService.addMenu(menu));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Menu>> updateDate(@RequestParam Long menuId, @Valid @RequestBody Menu newMenu) {
        Menu menu = menuService.updateMenu(menuId, newMenu);
        ApiResponse<Menu> response = new ApiResponse<>("200", MessageResponseConstants.UPDATE_RESPONSE, menu);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Boolean>> deleteMenu(@RequestParam Long menuId) throws CustomException {
        boolean menu = menuService.deleteMenu(menuId);
        ApiResponse<Boolean> response = new ApiResponse<>(String.valueOf(HttpStatus.OK.value()), MessageResponseConstants.HEARD_DELETE_RESPONSE, menu);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
