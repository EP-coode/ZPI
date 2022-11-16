package com.core.backend.utilis;

import com.core.backend.exception.NoIdException;
import com.core.backend.exception.WrongIdException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
