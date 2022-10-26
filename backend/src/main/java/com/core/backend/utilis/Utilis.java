package com.core.backend.utilis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Utilis {

    public long convertId(String stringId) throws NoIdException, WrongIdException {
        if (stringId == null)
            throw new NoIdException("Brak wartości dla pola id");
        long longId;
        try {
            longId = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new WrongIdException("Podane id nie jest liczbą");
        }
        return longId;
    }
}
