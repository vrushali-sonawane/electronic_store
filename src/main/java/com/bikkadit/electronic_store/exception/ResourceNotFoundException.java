package com.bikkadit.electronic_store.exception;

import com.bikkadit.electronic_store.payload.AppConstants;
import lombok.Builder;

@Builder
public class ResourceNotFoundException  extends RuntimeException{



    public ResourceNotFoundException() {
        super("Resource Not Found  !!");
    }

    public ResourceNotFoundException(String message) {

        super(message);
    }


}
