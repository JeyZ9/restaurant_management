package com.app.restaurant_management.services.impl;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.commons.exception.ResourceNotFoundException;
import com.app.restaurant_management.models.Tables;
import com.app.restaurant_management.repository.TableRepository;
import com.app.restaurant_management.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableServiceImpl implements BaseService<Tables, Long> {
    private final TableRepository tableRepository;

    @Autowired
    public TableServiceImpl(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public List<Tables> getAll() throws CustomException {
        List<Tables> tables = tableRepository.findAll();
        if(tables.isEmpty()){
            throw new CustomException(String.format(MessageResponseConstants.DATA_NOT_FOUND_RESPONSE, "Table"));
        }
        return tables;
    }

    @Override
    public Tables getById(Long tableId) {
        return tableRepository.findById(tableId).orElseThrow(() -> new ResourceNotFoundException("Tables", "id", String.valueOf(tableId)));
    }

    @Override
    public Tables create(Tables table) {
        return tableRepository.save(table);
    }

    @Override
    public Tables update(Long tableId, Tables newTable){
        Tables table = tableRepository.findById(tableId).orElseThrow(() -> new ResourceNotFoundException("Tables", "id", String.valueOf(tableId)));
        table.setIsAvailable(newTable.getIsAvailable());
        table.setNumberOfSeats(newTable.getNumberOfSeats());
        return tableRepository.save(table);
    }

    @Override
    public boolean delete(Long tableId) throws CustomException {
        try{
            Tables table = tableRepository.findById(tableId).orElseThrow(() -> new ResourceNotFoundException("Tables", "id", String.valueOf(tableId)));
            tableRepository.delete(table);
            return true;
        }catch (RuntimeException ex) {
            throw ex;
        }catch (Exception ex){
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }
    }
}
