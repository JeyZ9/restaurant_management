package com.app.restaurant_management.services;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.models.Menu;
import com.app.restaurant_management.repository.MenuRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public MenuService(MenuRepository menuRepository, ObjectMapper objectMapper){
        this.menuRepository = menuRepository;
        this.objectMapper = objectMapper;
    }

    public List<Menu> getAllMenus() throws CustomException {
        List<Menu> menus = menuRepository.findAll();
        if(menus.isEmpty()){
            throw new CustomException(String.format(MessageResponseConstants.DATA_NOT_FOUND_RESPONSE, "Menu"));
        }
        return menus;
    }

    public Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", String.valueOf(menuId)));

    }

    public List<Menu> getMenuByName(String menuName) throws CustomException {
        return  getAllMenus().stream().filter(menu -> menu.getMenuName().toLowerCase().contains(menuName.toLowerCase())).toList();
    }

    public Menu addMenu(Menu menu) throws JsonProcessingException {
        logger.debug("Object menu: {}", objectMapper.writeValueAsString(menu));
        return menuRepository.save(menu);
    }

    public Menu updateMenu(Long menuId, Menu newMenu){
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", String.valueOf(menuId)));
        menu.setMenuName(newMenu.getMenuName());
        return menuRepository.save(menu);
    }

    public boolean deleteMenu(Long menuId) throws CustomException {
        try {
            Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", String.valueOf(menuId)));
            menuRepository.delete(menu);
            logger.info("Deleted menu with ID: {}", menuId);
            return true;
        }catch (RuntimeException ex){
            throw ex;
        }catch (Exception ex) {
            logger.error("Error delete menu with id {}: {}", menuId, ex.getMessage(), ex);
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }
    }
}
