/*
 *  Copyright (c) Lightstreamer Srl
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.lightstreamer.jms_extender.hooks;

/**
 * Exception class used by the hook to signal an
 * error condition during initialization or
 * request methods.
 */
public class HookException extends Exception {
    private String _errorCode;

    /**
     * Constructs a HookException with the specified detail message.
     * 
     * @param message the detail message.
     */
    public HookException(String message) {
        super(message);
    }
    
    /**
     * Constructs a HookException with the specified detail message and error code.
     * 
     * @param message the detail message.
     * @param errorCode the specific error code.
     */
    public HookException(String message, String errorCode) {
        super(message);
        
        _errorCode= errorCode;
    }

    /**
     * Constructs a new exception with the specified detail message and 
     * cause.
     *
     * @param message the detail message.
     * @param cause the cause.
     */
    public HookException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified detail message, error code
     * and cause.
     *
     * @param message the detail message.
     * @param errorCode the specific error code.
     * @param cause the cause.
     */
    public HookException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        
        _errorCode= errorCode;
    }
    
    /**
     * Returns the specific error code of this exception.
     * 
     * @return the error code.
     */
    public String getErrorCode() {
        return _errorCode;
    }
}
