/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tag;

import javacard.framework.*;

/**
 *
 * @author Simon
 */
public class Tag4 extends Applet {
        
    private byte[] CCfile = new byte[]{
        (byte)0x00, (byte)0x0F, // CCLEN 2 bytes 
        (byte)0x20,             // Mapping Version 1 byte
        (byte)0x00, (byte)0x3B, // MLe Max R-APDU
        (byte)0x00, (byte)0x34, // MLc Max C-APDU
            // TLV
            (byte)0xE1, (byte)0x04, // TLV File identifyer
            (byte)0x00, (byte)0x32, // TLV Max NDEF size
            (byte)0x00,             // NDEF file read access condition
            (byte)0x00              // NDEF file write access condition
    };
    
    private byte[] NDEFfile;

    /**
     * Installs this applet.
     * 
     * @param bArray
     *            the array containing installation parameters
     * @param bOffset
     *            the starting offset in bArray
     * @param bLength
     *            the length in bytes of the parameter data in bArray
     */
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Tag4();
    }

    /**
     * Only this class's install method should create the applet object.
     */
    protected Tag4() {
        register();
    }

    /**
     * Processes an incoming APDU.
     * 
     * @see APDU
     * @param apdu
     *            the incoming APDU
     */
    public void process(APDU apdu) { 
        if (selectingApplet()){
            return;
        }
    }
}
