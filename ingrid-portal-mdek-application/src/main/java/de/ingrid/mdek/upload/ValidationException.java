/*-
 * **************************************************-
 * InGrid Portal MDEK Application
 * ==================================================
 * Copyright (C) 2014 - 2020 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.mdek.upload;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

public class ValidationException extends WebApplicationException implements UploadException {

    private static final long serialVersionUID = 1L;
    private static final String FILE_KEY = "file";

    protected final Map<String, Object> data = new HashMap<>();

    /**
     * Constructor
     * @param message
     * @param file
     * @param status
     */
    public ValidationException(final String message, final String file, final int statusCode) {
        super(message, statusCode);
        this.data.put(FILE_KEY, file);
    }

    @Override
    public Map<String, Object> getData() {
        return this.data;
    }
}
