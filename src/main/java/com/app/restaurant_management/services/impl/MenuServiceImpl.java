package com.app.restaurant_management.services.impl;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.models.Menu;
import com.app.restaurant_management.repository.MenuRepository;
import com.app.restaurant_management.services.BaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements BaseService<Menu, Long> {

    private final MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, ObjectMapper objectMapper){
        this.menuRepository = menuRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Menu> getAll() throws CustomException {
        List<Menu> menus = menuRepository.findAll();
        if(menus.isEmpty()){
            throw new CustomException(String.format(MessageResponseConstants.DATA_NOT_FOUND_RESPONSE, "Menu"));
        }
        return menus;
    }

    @Override
    public Menu getById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", String.valueOf(menuId)));

    }

    public List<Menu> getByName(String menuName) throws CustomException {
        return  getAll().stream().filter(menu -> menu.getMenuName().toLowerCase().contains(menuName.toLowerCase())).toList();
    }

    @Override
    public Menu create(Menu menu) throws JsonProcessingException {
        logger.debug("Object menu: {}", objectMapper.writeValueAsString(menu));
        return menuRepository.save(menu);
    }

    @Override
    public Menu update(Long menuId, Menu newMenu){
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", String.valueOf(menuId)));
        menu.setMenuName(newMenu.getMenuName());
        return menuRepository.save(menu);
    }

    @Override
    public boolean delete(Long menuId) throws CustomException {
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
