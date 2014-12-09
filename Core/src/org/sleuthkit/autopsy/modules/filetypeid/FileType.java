/*
 * Autopsy Forensic Browser
 *
 * Copyright 2014 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.modules.filetypeid;

import java.util.Arrays;
import java.util.logging.Level;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Represents a named file type characterized by a file signature.
 */
final class FileType {

    private final String mimeType;
    private final Signature signature;
    private final String filesSetName;
    private final boolean alert;

    /**
     * Creates a representation of a named file type characterized by a file
     * signature.
     *
     * @param mimeType The mime type to associate with this file type.
     * @param signature The signature that characterizes the file type.
     * @param filesSetName The interesting files set name
     * @param alert A flag indicating whether the user wishes to be alerted when
     * a file matching this type is encountered.
     */
    FileType(String mimeType, final Signature signature, String filesSetName, boolean alert) {
        this.mimeType = mimeType;
        this.signature = new Signature(signature.getSignatureBytes(), signature.getOffset(), signature.getType());
        this.filesSetName = filesSetName;
        this.alert = alert;
    }

    /**
     * Gets the MIME type associated with this file type.
     *
     * @return The type name.
     */
    String getMimeType() {
        return this.mimeType;
    }

    /**
     * Gets the signature associated with this file type.
     *
     * @return The file signature.
     */
    Signature getSignature() {
        return new Signature(this.signature.getSignatureBytes(), this.signature.getOffset(), this.signature.getType());
    }

    /**
     * Determines whether or not a given file is an instance of this file type.
     *
     * @param file The file to test.
     * @return True or false.
     */
    boolean matches(final AbstractFile file) {
        return this.signature.containedIn(file);
    }

    /**
     * Indicates whether or not an alert is desired if a file of this type is
     * encountered.
     *
     * @return True or false.
     */
    boolean alertOnMatch() {
        return this.alert;
    }
    
    /**
     * Gets the interesting files set name assigned to this file type
     * @return 
     */
    String getFilesSetName() {
        return this.filesSetName;
    }

    /**
     * A file signature consisting of a sequence of bytes at a specific offset
     * within a file.
     */
    static final class Signature {

        private static final Logger logger = Logger.getLogger(Signature.class.getName());

        /**
         * The way the signature byte sequence should be interpreted.
         */
        enum Type {

            RAW, ASCII
        };

        private final byte[] signatureBytes;
        private final long offset;
        private final Type type;

        /**
         * Creates a file signature consisting of a sequence of bytes at a
         * specific offset within a file.
         *
         * @param signatureBytes The signature bytes.
         * @param offset The offset of the signature bytes.
         * @param type The interpretation of the signature bytes (e.g., raw
         * bytes, an ASCII string).
         */
        Signature(final byte[] signatureBytes, long offset, Type type) {
            this.signatureBytes = Arrays.copyOf(signatureBytes, signatureBytes.length);
            this.offset = offset;
            this.type = type;
        }

        /**
         * Gets the byte sequence of the signature.
         *
         * @return The byte sequence as an array of bytes.
         */
        byte[] getSignatureBytes() {
            return Arrays.copyOf(this.signatureBytes, this.signatureBytes.length);
        }

        /**
         * Gets the offset of the signature.
         *
         * @return The offset.
         */
        long getOffset() {
            return this.offset;
        }

        /**
         * Gets the interpretation of the byte sequence for the signature.
         *
         * @return The signature type.
         */
        Type getType() {
            return this.type;
        }

        /**
         * Determines whether or not the signature is contained within a given
         * file.
         *
         * @param file The file to test
         * @return True or false.
         */
        boolean containedIn(final AbstractFile file) {
            try {
                byte[] buffer = new byte[this.signatureBytes.length];
                int bytesRead = file.read(buffer, offset, this.signatureBytes.length);
                return ((bytesRead == this.signatureBytes.length) && (Arrays.equals(buffer, this.signatureBytes)));
            } catch (TskCoreException ex) {
                Signature.logger.log(Level.WARNING, "Error reading from file with objId = " + file.getId(), ex);
                return false;
            }
        }
    }

}
